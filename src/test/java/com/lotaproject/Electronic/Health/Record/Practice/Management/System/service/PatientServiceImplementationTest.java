package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.MedicalHistory;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.ElectronicHealthException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.token.ConfirmationTokenService;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceImplementationTest {

    @Mock
    private MedicalHistoryService medicalHistoryService;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private BCryptPasswordEncoder encoder;
    @Mock
    private Configuration configuration;

    @InjectMocks
    private PatientServiceImplementation patientService;

    private RegisterPatientRequest request;

    @BeforeEach
    public void setUp() {
        request = new RegisterPatientRequest();
        request.setEmail("test@test.com");
        request.setPassword("Test@123");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setMedicalHistory(new MedicalHistory());
    }

    @Test
    public void testRegisterPatientWhenRequestIsValidThenPatientIsRegistered() throws IOException, TemplateException {
        when(encoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(patientRepository.save(any(Patient.class))).thenReturn(new Patient());
        when(configuration.getTemplate(anyString())).thenReturn(null);
        when(FreeMarkerTemplateUtils.processTemplateIntoString(any(), any())).thenReturn("emailContent");

        ApiResponse<?> response = patientService.registerPatient(request);

        assertNotNull(response);
        assertEquals("Registration Successfully", response.getMessage());
        assertNotNull(response.getToken());
        assertNotNull(response.getData());
    }

    @Test
    public void testRegisterPatientWhenEmailIsInvalidThenExceptionIsThrown() {
        request.setEmail("invalidEmail");

        assertThrows(ElectronicHealthException.class, () -> patientService.registerPatient(request));
    }

    @Test
    public void testRegisterPatientWhenEmailExistsThenExceptionIsThrown() {
        when(patientRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(ElectronicHealthException.class, () -> patientService.registerPatient(request));
    }

    @Test
    public void testRegisterPatientWhenPasswordIsInvalidThenExceptionIsThrown() {
        request.setPassword("invalidPassword");

        assertThrows(ElectronicHealthException.class, () -> patientService.registerPatient(request));
    }
}