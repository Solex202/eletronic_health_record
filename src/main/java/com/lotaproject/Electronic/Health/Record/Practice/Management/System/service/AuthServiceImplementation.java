package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.UserDetailsImpl;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.AuthException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.security.JwtService;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.security.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ExceptionMessages.*;
@Service
@Slf4j
public class AuthServiceImplementation implements AuthService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PatientRepository patientRepository;

//    @Autowired
//    RoleRepository roleRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    JwtService jwtService;

    public Patient findByEmail(String email) {
        return patientRepository.findByEmail(email).orElseThrow(()-> new PatientDoesNotexistException(String.format(PATIENT_WITH_EMAIL_DOESNOT_EXIST.getMessage(), email)));
    }
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
//        StringBuilder builder = new StringBuilder();
//        if(!loginRequest.getEmail().matches()) builder.append(INCORRECT_EMAIL.getMessage()).append("\n");
//        if(!loginRequest.getPassword().matches(patient.getPassword())) builder.append(INCORRECT_PASSWORD.getMessage());
//
//        if(!builder.isEmpty()){
//            throw new AuthException(builder.toString());
//        }

        try{

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtService.generateToken(userDetails);
        List<String> roles = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Patient patient = findByEmail(loginRequest.getEmail());
        patient.setLoginStatus(true);
        patientRepository.save(patient);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return LoginResponse.builder()
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .jwt(jwt)
                .email(userDetails.getUsername())
                .id(userDetails.getId())
                .build();
        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }
}
