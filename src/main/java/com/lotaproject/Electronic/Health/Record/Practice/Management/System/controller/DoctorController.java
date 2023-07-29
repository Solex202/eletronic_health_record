package com.lotaproject.Electronic.Health.Record.Practice.Management.System.controller;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.service.DoctorService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @PostMapping("/register")
    public ResponseEntity<?> registerDoctor(@RequestBody Doctor doctor){
        try {
            ApiResponse<?>response = doctorService.saveDoctor(doctor);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ElectronicHealthException | IOException | TemplateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
