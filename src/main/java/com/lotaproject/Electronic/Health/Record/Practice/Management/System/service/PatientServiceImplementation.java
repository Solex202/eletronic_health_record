package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.UpdatePatientDetailRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.PaginatedPatientResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.*;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.CannotRegisterPatientException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ExceptionMessages.*;

@Service
@AllArgsConstructor
@Slf4j
public class PatientServiceImplementation implements PatientService{
    private final MedicalHistoryService medicalHistoryService;
    @Autowired
    private  PatientRepository patientRepository;
    @Override
    public ApiResponse<?> registerPatient(RegisterPatientRequest request) {
        var patient = new Patient();

        String patientIdentity = RandomString.make(7);

        registerPatientValidation(request);

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
        patient.setPassword(request.getPassword());
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
    private void registerPatientValidation(RegisterPatientRequest request) {
        StringBuilder builder = new StringBuilder();
        if(!emailIsValid(request.getEmail())) builder.append(EMAIL_IS_INVALID.getMessage()).append("\n");

        if(patientRepository.existsByEmail(request.getEmail())) builder.append(EMAIL_ALREADY_EXCEPTION.getMessage()).append("\n");
        if(!passwordIsValid(request.getPassword())) builder.append(INVALID_PASSWORD.getMessage());

        if(!builder.isEmpty()) throw new ElectronicHealthException(builder.toString());
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

        updateEmailValidation(request1.getEmail(), patient);

        if(request1.getEmail() != null) patient.setEmail(request1.getEmail());
        if(request1.getFirstName() != null) patient.setFirstName(request1.getFirstName());
        if(request1.getDob() != null) patient.setDob(LocalDate.parse(request1.getDob()));
        if(request1.getLastName() != null) patient.setLastName(request1.getLastName());
        if(request1.getOccupation() != null) patient.setOccupation(request1.getOccupation());

        var newPatient = patientRepository.save(patient);
        return ApiResponse.builder().message("Updated Successful").data(newPatient).build();
    }
    @Override
    public PaginatedPatientResponse findByName(int pageNumber, int pageSize,String name) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "registeredDate");
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(order));

        Page<Patient> listOfPatient = patientRepository.findByFirstNameContainingOrLastNameContaining(name, name,pageable);
        log.info("list of patients ----> {}", listOfPatient.toList());
        return PaginatedPatientResponse.builder()
                .patients(listOfPatient.toList())
                .noOfPatients(listOfPatient.getContent().size())
                .currentPage(pageNumber)
                .pageSize(pageSize)
                .build();
    }

    private void updateEmailValidation(String email, Patient patient) {
        if(!emailIsValid(email)) throw new CannotRegisterPatientException(EMAIL_IS_INVALID.getMessage());

        if(patientRepository.existsByEmail(email)){
            if(Objects.equals(email, patient.getEmail())){
                patient.setEmail(email);
            }else throw new CannotRegisterPatientException(EMAIL_ALREADY_EXCEPTION.getMessage());
        }
    }

    private boolean emailIsValid(String email) {

        String regex = "[a-zA-z][\\w-]{1,20}@\\w{2,20}\\.\\w{2,3}$";
        Pattern pattern  = Pattern.compile(regex);
        Matcher matcher =  pattern.matcher(email);

        return matcher.matches();
    }

    private boolean passwordIsValid(String password) {
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20}$";
        Pattern pattern  = Pattern.compile(regex);
        Matcher matcher =  pattern.matcher(password);

        return matcher.matches();
    }
}
