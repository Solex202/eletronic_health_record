package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Role;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.email.EmailSender;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.token.ConfirmationToken;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.token.ConfirmationTokenService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ExceptionMessages.*;

@Service
@AllArgsConstructor
@Slf4j
public class DoctorServiceImplementation implements DoctorService{

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    Configuration configuration;
    @Autowired
    private DoctorRepository doctorRepository;
    private final BCryptPasswordEncoder encoder;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Autowired
    private EmailSender emailSender;
    @Override
    public ApiResponse<?> saveDoctor(Doctor doctor) throws IOException, TemplateException {

        if(!emailIsValid(doctor.getEmail())) throw new ElectronicHealthException(EMAIL_IS_INVALID.getMessage());
        if(doctorRepository.existsByEmail(doctor.getEmail())) throw new ElectronicHealthException(EMAIL_ALREADY_EXCEPTION.getMessage());
        //TODO: check if attributes are the same and if email not confirmed, send another email
        // how do i know that an email is not confirmed?
        if(!passwordIsValid(doctor.getPassword())) throw new ElectronicHealthException(INVALID_PASSWORD.getMessage());
        String encodedPassword = encoder.encode(doctor.getPassword());

        Set<Role> role = new HashSet<>();
        role.add(Role.DOCTOR);
        String doctorIdentity = RandomString.make(7);
        doctor.setRegisteredDate(LocalDateTime.now());
        doctor.setModifiedDate(LocalDateTime.now());
        doctor.setPassword(encodedPassword);
        doctor.setUniqueId(doctorIdentity);
        doctor.setRoles(role);
        Doctor newDoctor = doctorRepository.save(doctor);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15),null, newDoctor);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        var builder = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        map.put("name", doctor.getFirstName() + " "+ doctor.getLastName());
        map.put("token", token);
        builder.append(FreeMarkerTemplateUtils.processTemplateIntoString(configuration.getTemplate("doctorservice.ftlh"), map)
        );

        emailSender.send(doctor.getEmail(), builder.toString());

        return ApiResponse.builder().message("Successful").data(newDoctor).build();
    }

    @Override
    public List<Doctor> findAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public String deleteDoctorFromDatabase(String doctorId) {

         doctorRepository.deleteById(doctorId);
         return "successful";
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
