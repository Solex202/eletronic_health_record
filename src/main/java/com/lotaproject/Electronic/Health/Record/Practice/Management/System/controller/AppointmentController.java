package com.lotaproject.Electronic.Health.Record.Practice.Management.System.controller;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.BookAppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.AppointmentForm;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("book/{patientId}")
    public ResponseEntity<?> bookAppointment(@PathVariable String patientId,  @RequestBody BookAppointmentFormDto bookAppointmentFormDto) {
        try {
            ApiResponse<?> response = appointmentService.bookAppointment(patientId, bookAppointmentFormDto);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("get-available-doctors")
    public ResponseEntity<?> getAvailableDoctors(@PathVariable LocalDate localDate){
        try {
            List<String> doctors = appointmentService.getAvailableDoctors(localDate);
            return new ResponseEntity<>(doctors, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("get-times")
    public ResponseEntity<?> getTimeSlots(@RequestParam(value = "doctorName") String doctorName, @RequestParam(value = "date") String date){
        try {
            List<LocalTime> timeList = appointmentService.getDoctorTimeSlots(doctorName, date);
            return new ResponseEntity<>(timeList, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> findAll(){
        try{
            List<AppointmentForm> appointmentFormList = appointmentService.findAll();
            return new ResponseEntity<>(appointmentFormList, HttpStatus.OK);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
