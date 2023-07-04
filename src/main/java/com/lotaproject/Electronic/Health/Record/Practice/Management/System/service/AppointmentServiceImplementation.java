package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.AppointmentForm;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.AppointmentRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class AppointmentServiceImplementation implements AppointmentService{
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientRepository patientRepository;

    @Override
    public ApiResponse<?> bookAppointment(String id, AppointmentForm form) {
         var patient = patientRepository.findById(id);

         var appointForm = new AppointmentForm();
         appointForm.setTimeSlot(form.getTimeSlot());
         appointForm.setAppointmentDate(form.getAppointmentDate());
         appointForm.setPatientID(patient.get().getPatientId());
         appointForm.setDoctorName(form.getDoctorName());
         appointForm.setBookedTime(LocalDateTime.now());
         appointForm.setPatientName(form.getPatientName());

         AppointmentForm newForm = appointmentRepository.save(appointForm);

        return  ApiResponse.builder().message("Appointment Booked successfully").data(newForm).build();
    }
}
