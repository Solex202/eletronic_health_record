package com.lotaproject.Electronic.Health.Record.Practice.Management.System.controller;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctors")
public class DoctorController {

    @PostMapping("/register")
    public void registerDoctor(@RequestBody Doctor doctor){
        try {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
