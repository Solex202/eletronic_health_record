package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.UserDetailsImpl;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.security.JwtService;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.security.RefreshToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        log.info("AUTHENTICATION ----> {}", authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("USER DETAILS ----> {}",userDetails);

        String jwt = jwtService.generateToken(userDetails);

        log.info("JWT -----> {}", jwt);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        log.info("ROLE ---> {}", roles);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        log.info("REFRESH TOKEN ----> {}", refreshToken);

        return LoginResponse.builder()
                .refreshToken(refreshToken.getToken())
                .roles(roles)
                .jwt(jwt)
                .email(userDetails.getUsername())
                .id(userDetails.getId())
                .build();
    }
}
