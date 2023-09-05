package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.UpdatePatientDetailRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.PaginatedPatientResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.*;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.email.EmailSender;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.CannotRegisterPatientException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.token.ConfirmationToken;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.token.ConfirmationTokenService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ExceptionMessages.*;

@Service
@AllArgsConstructor
@Slf4j
public class PatientServiceImplementation implements PatientService{
    private final MedicalHistoryService medicalHistoryService;
    @Autowired
    private  EmailSender emailSender;
    @Autowired
    Configuration configuration;
    private final BCryptPasswordEncoder encoder;
    @Autowired
    private  PatientRepository patientRepository;

    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Override
    public ApiResponse<?> registerPatient(RegisterPatientRequest request) throws IOException, TemplateException {

        var builder = new StringBuilder();

        String patientIdentity = RandomString.make(7);
        Set<Role> role = new HashSet<>();
        role.add(Role.PATIENT);

        String encodedPassword = encoder.encode(request.getPassword());

        registerPatientValidation(request);

        MedicalHistory medicalHistory = MedicalHistory.builder().medication(request.getMedicalHistory().getMedication())
                .allergy(request.getMedicalHistory().getAllergy()).attachment(request.getMedicalHistory().getAttachment())
                .ailment(request.getMedicalHistory().getAilment()).createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now()).build();


        medicalHistoryService.createMedicalHistory(medicalHistory);

        var patient = Patient.builder().medicalHistory(medicalHistory).roles(role).patientId(patientIdentity)
                .address(request.getAddress()).email(request.getEmail()).password(encodedPassword)
                .firstName(request.getFirstName()).lastName(request.getLastName()).phoneNumber(request.getPhoneNumber())
                .occupation(request.getOccupation()).gender(Gender.valueOf(request.getGender()))
                .bloodGroup(BloodGroup.valueOf(request.getBloodGroup())).genotype(Genotype.valueOf(request.getGenotype()))
                .dob(LocalDate.parse(request.getDob())).guardian(request.getGuardianPhoneNumber())
                .registeredDate(LocalDateTime.now()).modifiedDate(LocalDateTime.now()).build();

        var savedPatient = patientRepository.save(patient);
        String token = sendMailAndConfirmationToken(request, builder, savedPatient);

        return ApiResponse.builder().message("Registration Successfully").token(token).data(savedPatient).build();

    }

    private String sendMailAndConfirmationToken(RegisterPatientRequest request, StringBuilder builder, Patient savedPatient) throws IOException, TemplateException {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),null, savedPatient);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        Map<String, Object> map = new HashMap<>();
        map.put("name", request.getFirstName() + " "+ request.getLastName());
        map.put("token", token);
        builder.append(FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("patientservice.ftlh"), map)
        );

        emailSender.send(request.getEmail(), builder.toString());
        return token;
    }

    private void registerPatientValidation(RegisterPatientRequest request) {
        StringBuilder builder = new StringBuilder();
        if(!emailIsValid(request.getEmail())) builder.append(EMAIL_IS_INVALID.getMessage()).append("\n");

        if(patientRepository.existsByEmail(request.getEmail())) builder.append(EMAIL_ALREADY_EXCEPTION.getMessage()).append("\n");
        //TODO: check if attributes are the same and if email not confirmed, send another email
        // how do i know that an email is not confirmed?

        ValidationService sa = new ValidationService();

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
        return ApiResponse.builder().message(UPDATE_MESSAGE.getMessage()).data(newPatient).build();
    }
    @Override
    public PaginatedPatientResponse findByName(int pageNumber, int pageSize,String name) {
        Sort.Order order = new Sort.Order(Sort.Direction.DESC, "registeredDate");
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, Sort.by(order));

        Page<Patient> listOfPatient = patientRepository.findByFirstNameContainingOrLastNameContaining(name, name,pageable);
        return PaginatedPatientResponse.builder()
                .patients(listOfPatient.toList())
                .noOfPatients(listOfPatient.getContent().size())
                .currentPage(pageNumber)
                .pageSize(pageSize)
                .build();
    }

    @Override
    public List<Patient> findAllPatients() {
        return patientRepository.findAll();
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
