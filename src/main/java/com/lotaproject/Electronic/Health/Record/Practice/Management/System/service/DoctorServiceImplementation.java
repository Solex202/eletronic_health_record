package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.LoginException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ExceptionMessages.*;

@Service

public class DoctorServiceImplementation implements DoctorService{

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DoctorRepository doctorRepository;
    @Override
    public ApiResponse<?> saveDoctor(Doctor doctor) {
        if(!emailIsValid(doctor.getEmail())) throw new ElectronicHealthException(EMAIL_IS_INVALID.getMessage());
        if(doctorRepository.existsByEmail(doctor.getEmail())) throw new ElectronicHealthException(EMAIL_ALREADY_EXCEPTION.getMessage());
        if(!passwordIsValid(doctor.getPassword())) throw new ElectronicHealthException(INVALID_PASSWORD.getMessage());
        Doctor newDoctor = doctorRepository.save(doctor);

        return ApiResponse.builder().message("Successful").data(newDoctor).build();
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
