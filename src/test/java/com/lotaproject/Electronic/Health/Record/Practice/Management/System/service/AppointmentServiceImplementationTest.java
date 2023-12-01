package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.BookAppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.AppointmentForm;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.AppointmentRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.AppointmentException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.DoctorException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AppointmentServiceImplementationTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AppointmentServiceImplementation appointmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testViewAppointmentWhenValidIdThenReturnAppointmentForm() {
        String appointmentId = "1";
        AppointmentForm mockAppointmentForm = new AppointmentForm();
        when(appointmentRepository.findById(anyString())).thenReturn(Optional.of(mockAppointmentForm));

        AppointmentForm returnedAppointmentForm = appointmentService.viewAppointment(appointmentId);

        assertEquals(mockAppointmentForm, returnedAppointmentForm, "The returned appointment form should be the same as the mock appointment form");
    }

    public void testGetDoctorById(){
        String doctorId = "123";
        Doctor mockedDoctor = new Doctor();
        when(doctorRepository.findById(anyString())).thenReturn(Optional.of(mockedDoctor));

        //act
        Doctor returnedDoctor = appointmentService.getDoctor(doctorId);
    }

    @Test
    public void testViewAppointmentWhenInvalidIdThenThrowAppointmentException() {
        String appointmentId = "1";
        when(appointmentRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(AppointmentException.class, () -> appointmentService.viewAppointment(appointmentId), "An AppointmentException should be thrown if the appointment ID does not exist");
    }

    @Test
    public void testBookAppointmentWhenValidPatientIdAndFormThenAppointmentBooked() throws Exception {
        String patientId = "1";
        Patient patient = new Patient();
        patient.setFirstName("name");
        patient.setLastName("last");
        patient.setPatientId(patientId);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        BookAppointmentFormDto form = new BookAppointmentFormDto();
        form.setAppointmentDate(LocalDate.now());
        form.setAppointmentTime(LocalTime.now());
        form.setDoctorName("Doctor");

        appointmentService.bookAppointment(patientId, form);

        verify(appointmentRepository, times(1)).save(any(AppointmentForm.class));
    }

    @Test
    public void testBookAppointmentWhenInvalidPatientIdThenExceptionThrown() {
        String patientId = "1";
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        BookAppointmentFormDto form = new BookAppointmentFormDto();
        form.setAppointmentDate(LocalDate.now());
        form.setAppointmentTime(LocalTime.now());
        form.setDoctorName("Doctor");

        assertThatThrownBy(() -> appointmentService.bookAppointment(patientId, form))
                .isInstanceOf(PatientDoesNotexistException.class);
    }

    @Test
    public void testBookAppointmentWhenDoctorWithEmailDoesNotExistThenExceptionThrown() throws Exception {
        String patientId = "1";
        Patient patient = new Patient();
        patient.setFirstName("name");
        patient.setLastName("last");
        patient.setPatientId(patientId);
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        BookAppointmentFormDto form = new BookAppointmentFormDto();
        form.setAppointmentDate(LocalDate.now());
        form.setAppointmentTime(LocalTime.now());
        form.setDoctorName("Doctor");

        when(doctorRepository.findByEmail(form.getDoctorName())).thenReturn(Optional.empty());

        assertThrows(DoctorException.class, () -> appointmentService.bookAppointment(patientId, form));
    }
}