package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.BookAppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.*;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.AppointmentRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRegistryRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.email.EmailSender;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.AppointmentException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.DoctorException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ExceptionMessages.DOCTOR_WITH_EMAIL_DOESNOT_EXIST;
import static com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ExceptionMessages.PATIENT_WITH_ID_DOESNOT_EXIST;

@Service
@AllArgsConstructor
@Slf4j
public class AppointmentServiceImplementation implements AppointmentService{
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorRegistryRepository doctorRegistryRepository;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    Configuration configuration;

    private Patient getPatient(String id){
        return patientRepository.findById(id).orElseThrow(()-> new PatientDoesNotexistException(String.format(PATIENT_WITH_ID_DOESNOT_EXIST.getMessage(), id)));
    }
    public Doctor getDoctor(String email){
        return doctorRepository.findByEmail(email).orElseThrow(()-> new DoctorException(String.format(DOCTOR_WITH_EMAIL_DOESNOT_EXIST.getMessage(), email)));
    }

    public AppointmentForm getAppointment(String id){
        return appointmentRepository.findById(id).orElseThrow(()-> new AppointmentException("Appointment doesn't exist"));
    }

    @Override
    public ApiResponse<?> bookAppointment(String patientId, BookAppointmentFormDto form) throws IOException, TemplateException {
         Patient patient = getPatient(patientId);

         AppointmentForm appointForm = AppointmentForm.builder().appointmentDate(form.getAppointmentDate().toString())
                 .appointmentTime(form.getAppointmentTime().toString()).patientID(patient.getPatientId())
                 .doctorName(form.getDoctorName()).bookedTime(LocalDateTime.now()).patientName(patient.getFirstName().concat(" ").concat(patient.getLastName()))
                 .appointmentStatus(AppointmentStatus.BOOKED).duration("30 MINS").build();


         AppointmentForm newForm = appointmentRepository.save(appointForm);

//        sendEmailToPatient(patient, newForm);
//
//        var builder2 = new StringBuilder();
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("patientName", patient.getFirstName() + " "+ patient.getLastName());
//        map2.put("date", newForm.getAppointmentDate());
//        map2.put("time", newForm.getAppointmentTime());
//        map2.put("duration", newForm.getDuration());
//        map2.put("patientId", newForm.getPatientID());
//        builder2.append(FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("doctor.ftlh"), map2)
//        );
//
//        emailSender.send(patient.getEmail(), builder2.toString());

        return  ApiResponse.builder().message("Appointment Booked successfully").data(newForm).build();

    }

    private void sendEmailToPatient(Patient patient, AppointmentForm newForm) throws IOException, TemplateException {
        var builder = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        map.put("name", patient.getFirstName() + " "+ patient.getLastName());
        map.put("date", newForm.getAppointmentDate());
        map.put("time", newForm.getAppointmentTime());
        map.put("duration", newForm.getDuration());
        map.put("doctor", newForm.getDoctorName());
        builder.append(FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("appointment.ftlh"), map)
        );

        emailSender.send(patient.getEmail(), builder.toString());
    }

    public List<String> getAvailableDoctors(LocalDate date){
        List<DoctorRegistry> doctorRegistries = doctorRegistryRepository.findAll();
         List<String> doctorList = new ArrayList<>();
        for (DoctorRegistry doctorRegistry: doctorRegistries) {
            List<ScheduleRegistry> scheduleRegistries = doctorRegistry.getScheduleRegistries();
            for (ScheduleRegistry scheduleRegistry : scheduleRegistries) {
                if(scheduleRegistry.getFrom().toLocalDate().equals(date)){
                    Doctor doctor = getDoctor(doctorRegistry.getDoctorEmail());
                    String doctorName ="Doctor "+ doctor.getFirstName() + " " + doctor.getLastName();
                    String doctorEmail = doctorRegistry.getDoctorEmail();
                    doctorList.add(doctorName);
                    doctorList.add(doctorEmail);
                }
            }
        }

        return doctorList;
    }

    @Override
    public List<LocalTime> getDoctorTimeSlots( String doctorName, String date) {
        DoctorRegistry doctorRegistry = doctorRegistryRepository.findByDoctorEmail(doctorName);

        Map<String, List<LocalTime>> map = doctorRegistry.getThirtyMinutesInterval();

        return map.get(date);
    }

    @Override
    public ApiResponse<?> rescheduleAppointment(String patientId, String appointmentId, BookAppointmentFormDto form) throws TemplateException, IOException {

        AppointmentForm appointmentForm = getAppointment(appointmentId);
        Patient patient =getPatient(patientId);
        if(appointmentForm.getAppointmentStatus().equals(AppointmentStatus.COMPLETED)){
            throw new AppointmentException("Appointment already completed, cannot be rescheduled");
        }

        appointmentForm.setAppointmentDate(form.getAppointmentDate().toString());
        appointmentForm.setAppointmentTime(form.getAppointmentTime().toString());
        appointmentForm.setModifiedDate(LocalDateTime.now());
        appointmentForm.setPatientName(patient.getFirstName() + " " + patient.getLastName());
        appointmentForm.setPatientID(patient.getPatientId());
        appointmentForm.setAppointmentStatus(AppointmentStatus.BOOKED);
        appointmentForm.setDuration("30 MINS");

        AppointmentForm newForm = appointmentRepository.save(appointmentForm);

        sendEmailToPatient(patient, newForm);

        return  ApiResponse.builder().message("Appointment rescheduled successfully").data(newForm).build();

        //TODO: send mail to the patient and the doctor once the appointment is rescheduled successfully;
    }

    @Override
    public AppointmentForm cancelAppointment(String appointmentId) {
        AppointmentForm appointmentForm = getAppointment(appointmentId);
        if(appointmentForm.getAppointmentStatus() != AppointmentStatus.COMPLETED || appointmentForm.getAppointmentStatus() != AppointmentStatus.CANCELLED) {
            appointmentForm.setAppointmentStatus(AppointmentStatus.CANCELLED);
            appointmentForm.setModifiedDate(LocalDateTime.now());
        }
        appointmentRepository.save(appointmentForm);

        return appointmentForm;
    }

    @Override
    public AppointmentForm viewAppointment(String id) {
        return getAppointment(id);
    }

    @Override
    public List<AppointmentForm> findAll() {
        return appointmentRepository.findAll();
    }
}
