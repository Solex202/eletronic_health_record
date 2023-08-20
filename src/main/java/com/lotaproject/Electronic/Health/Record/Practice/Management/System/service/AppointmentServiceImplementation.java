package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.BookAppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.*;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.AppointmentRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRegistryRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.DoctorException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private Patient getPatient(String id){
        return patientRepository.findById(id).orElseThrow(()-> new PatientDoesNotexistException(String.format(PATIENT_WITH_ID_DOESNOT_EXIST.getMessage(), id)));
    }
    private Doctor getDoctor(String email){
        return doctorRepository.findByEmail(email).orElseThrow(()-> new DoctorException(String.format(DOCTOR_WITH_EMAIL_DOESNOT_EXIST.getMessage(), email)));
    }

    @Override
    public ApiResponse<?> bookAppointment(String patientId, BookAppointmentFormDto form) {
         var patient = getPatient(patientId);

         var appointForm = new AppointmentForm();
         appointForm.setTimeSlot(form.getAppointmentTime());
         appointForm.setAppointmentDate(form.getAppointmentDate());
         appointForm.setPatientID(patient.getPatientId());
         appointForm.setDoctorName(form.getDoctorName());
         appointForm.setBookedTime(LocalDateTime.now());
         appointForm.setPatientName(patient.getFirstName().concat(" ").concat(patient.getLastName()));
         appointForm.setAppointmentStatus(AppointmentStatus.BOOKED);

         AppointmentForm newForm = appointmentRepository.save(appointForm);

        return  ApiResponse.builder().message("Appointment Booked successfully").data(newForm).build();
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

        log.info("DOCTORS ---->{}",doctorList);
        return doctorList;

    }

    @Override
    public List<LocalTime> getDoctorTimeSlots(String doctorName, String date) {
        DoctorRegistry doctorRegistry = doctorRegistryRepository.findByDoctorEmail(doctorName);

        Map<String, List<LocalTime>> map = doctorRegistry.getThirtyMinutesInterval();

        return map.get(date);
    }
}
