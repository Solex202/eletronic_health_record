package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.AppointmentForm;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.AppointmentRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ExceptionMessages.PATIENT_WITH_ID_DOESNOT_EXIST;

@Service
@AllArgsConstructor
public class AppointmentServiceImplementation implements AppointmentService{
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;

    private Patient getPatient(String id){
        return patientRepository.findById(id).orElseThrow(()-> new PatientDoesNotexistException(String.format(PATIENT_WITH_ID_DOESNOT_EXIST.getMessage(), id)));
    }

    @Override
    public ApiResponse<?> bookAppointment(String id, AppointmentForm form) {
         var patient = getPatient(id);

         var appointForm = new AppointmentForm();
         appointForm.setTimeSlot(form.getTimeSlot());
         appointForm.setAppointmentDate(form.getAppointmentDate());
         appointForm.setPatientID(patient.getPatientId());
         appointForm.setDoctorName(form.getDoctorName());
         appointForm.setBookedTime(LocalDateTime.now());
         appointForm.setPatientName(patient.getFirstName().concat(" ").concat(patient.getLastName()));

         AppointmentForm newForm = appointmentRepository.save(appointForm);

        return  ApiResponse.builder().message("Appointment Booked successfully").data(newForm).build();
    }
}
