package com.lotaproject.Electronic.Health.Record.Practice.Management.System.controller;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.UpdatePatientDetailRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.PaginatedPatientResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.CannotRegisterPatientException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/health-record")
public class PatientController {

    @Autowired
    private PatientService patientService;
    @PostMapping("/registration")
    public ResponseEntity<?> register(@RequestBody RegisterPatientRequest registerPatientRequest) {
        try {
            ApiResponse<?> response = patientService.registerPatient(registerPatientRequest);
            response.setStatusCode(201);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CannotRegisterPatientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }
        @PostMapping("/login")
        public ResponseEntity<?> login(@RequestBody LoginRequest request){

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }
    @GetMapping("/find-by-email/{email}")
    public ResponseEntity<?> findByEmail(@PathVariable String email){
        try{
            var patient = patientService.findByEmail(email);
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } catch (PatientDoesNotexistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<?> findById(@PathVariable String id){
        try{
            var patient = patientService.findById(id);
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } catch (PatientDoesNotexistException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("update/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody UpdatePatientDetailRequest request){
        try{
            ApiResponse<?> updateResponse = patientService.updatePatientDetails(id, request);
            return new ResponseEntity<>(updateResponse, HttpStatus.OK);
        } catch (CannotRegisterPatientException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("find-by-name/{pageNumber}/{pageSize}/{name}")
    public ResponseEntity<?> findByName(@PathVariable int pageNumber, @PathVariable int pageSize, @PathVariable String name){
        try {
            PaginatedPatientResponse response = patientService.findByName(pageNumber, pageSize, name);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
