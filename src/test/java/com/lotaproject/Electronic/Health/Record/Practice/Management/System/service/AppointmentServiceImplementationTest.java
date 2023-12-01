package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.BookAppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.AppointmentForm;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.AppointmentRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
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


        ApiResponse<?> appointmentForm = appointmentService.bookAppointment(patientId, form);

        verify(appointmentRepository, times(1)).save(any(AppointmentForm.class));
        assertThat(appointmentForm).isNotNull();
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

        assertThatThrownBy(() -> appointmentService.bookAppointment(patientId, form))
                .isInstanceOf(DoctorException.class);
    }
}