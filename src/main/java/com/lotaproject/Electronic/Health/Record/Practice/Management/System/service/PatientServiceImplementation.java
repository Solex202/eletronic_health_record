package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.UpdatePatientDetailRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.*;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.CannotRegisterPatientException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import lombok.AllArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ExceptionMessages.*;

@Service
@AllArgsConstructor
public class PatientServiceImplementation implements PatientService{
    private final MedicalHistoryService medicalHistoryService;
    @Autowired
    private  PatientRepository patientRepository;
    @Override
    public ApiResponse<?> registerPatient(RegisterPatientRequest request) {
        var patient = new Patient();

        String patientIdentity = RandomString.make(7);

        emailValidation(request.getEmail());

        MedicalHistory medicalHistory = new MedicalHistory();
        medicalHistory.setMedication(request.getMedicalHistory().getMedication());
        medicalHistory.setAllergy(request.getMedicalHistory().getAllergy());
        medicalHistory.setAttachment(request.getMedicalHistory().getAttachment());
        medicalHistory.setAilment(request.getMedicalHistory().getAilment());
        medicalHistory.setCreatedDate(LocalDateTime.now());
        medicalHistory.setModifiedDate(LocalDateTime.now());

        medicalHistoryService.createMedicalHistory(medicalHistory);

        patient.setMedicalHistory(medicalHistory);
        patient.setPatientId(patientIdentity);
        patient.setAddress(request.getAddress());
        patient.setEmail(request.getEmail());
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setPhoneNumber(request.getPhoneNumber());
        patient.setOccupation(request.getOccupation());
        patient.setGender(Gender.valueOf(request.getGender()));
        patient.setBloodGroup(BloodGroup.valueOf(request.getBloodGroup()));
        patient.setGenotype(Genotype.valueOf(request.getGenotype()));
        patient.setDob(LocalDate.parse(request.getDob()));
        patient.setGuardian(request.getGuardian());
        patient.setGuardianPhoneNumber(request.getGuardianPhoneNumber());
        patient.setRegisteredDate(LocalDateTime.now());
        patient.setModifiedDate(LocalDateTime.now());

        var savedPatient = patientRepository.save(patient);

        return ApiResponse.builder().message("Registration Successfully").data(savedPatient).build();

    }

    private void emailValidation(String email) {
        if(!emailIsValid(email)) throw new CannotRegisterPatientException(EMAIL_IS_INVALID.getMessage());

        if(patientRepository.existsByEmail(email)) throw new CannotRegisterPatientException(EMAIL_ALREADY_EXCEPTION.getMessage());
    }

    @Override
    public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email).orElseThrow(()-> new PatientDoesNotexistException(String.format(PATIENT_WITH_EMAIL_DOESNOT_EXIST.getMessage(), email)));
    }
    @Override
    public Patient findById(String id) {
        return patientRepository.findById(id).orElseThrow(()-> new PatientDoesNotexistException(String.format(PATIENT_WITH_ID_DOESNOT_EXIST.getMessage(), id)));
    }

    @Override
    public ApiResponse<?> updatePatientDetails(String id, UpdatePatientDetailRequest request1) {
        var patient = findById(id);

        emailValidation(request1.getEmail());

        if(request1.getEmail() != null) patient.setEmail(request1.getEmail());
        if(request1.getFirstName() != null) patient.setFirstName(request1.getFirstName());

        var newPatient = patientRepository.save(patient);
        return ApiResponse.builder().message("Updated Successful").data(newPatient).build();
    }

    private boolean emailIsValid(String email) {

        String regex = "[a-zA-z][\\w-]{1,20}@\\w{2,20}\\.\\w{2,3}$";
        Pattern pattern  = Pattern.compile(regex);
        Matcher matcher =  pattern.matcher(email);

        return matcher.matches();
    }
}
