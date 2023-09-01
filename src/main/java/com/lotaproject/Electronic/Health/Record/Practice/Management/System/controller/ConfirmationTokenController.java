package com.lotaproject.Electronic.Health.Record.Practice.Management.System.controller;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.service.PatientService;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.service.PatientServiceImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
public class ConfirmationTokenController {

    @Autowired
    private PatientServiceImplementation patientService;

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        try
        {
            return patientService.confirmToken(token);
        } catch (ElectronicHealthException e) {
            return e.getMessage();
        }
    }
}
