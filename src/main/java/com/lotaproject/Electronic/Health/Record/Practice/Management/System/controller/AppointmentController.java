package com.lotaproject.Electronic.Health.Record.Practice.Management.System.controller;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.BookAppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("book-appointment/{patientId}")
    public ResponseEntity<?> bookAppointment(@PathVariable String patientId,  @RequestBody BookAppointmentFormDto bookAppointmentFormDto) {
        try {
            ApiResponse<?> response = appointmentService.bookAppointment(patientId, bookAppointmentFormDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
