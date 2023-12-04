package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.BookAppointmentFormDto;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.*;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.AppointmentRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRegistryRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.PatientRepository;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.AppointmentException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.DoctorException;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.exceptions.PatientDoesNotexistException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
public class AppointmentServiceImplementationTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorRegistryRepository doctorRegistryRepository;

    @InjectMocks
    private AppointmentServiceImplementation appointmentService;

    private final List<ScheduleRegistry> scheduleRegistryList = new ArrayList<>();


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        List<BreakPeriod> breakPeriodList = new ArrayList<>();
        BreakPeriod breakPeriod1 = new BreakPeriod();
        breakPeriod1.setFrom(LocalTime.of(9, 0));
        breakPeriod1.setTo(LocalTime.of(11, 0));

        breakPeriodList.add(breakPeriod1);

        BreakPeriod breakPeriod2 = new BreakPeriod();
        breakPeriod2.setFrom(LocalTime.of(13, 0));
        breakPeriod2.setTo(LocalTime.of(14, 0));

        breakPeriodList.add(breakPeriod2);

        ScheduleRegistry scheduleRegistry1 = new ScheduleRegistry();
        scheduleRegistry1.setFrom(LocalDateTime.of(2023,9,23,9,0));
        scheduleRegistry1.setTo(LocalDateTime.of(2023, 9,23,17,0));
        scheduleRegistry1.setBreakPeriod(breakPeriodList);

        scheduleRegistryList.add(scheduleRegistry1);

        ScheduleRegistry scheduleRegistry2 = new ScheduleRegistry();
        scheduleRegistry2.setFrom(LocalDateTime.of(2023,9,24,11,0));
        scheduleRegistry2.setTo(LocalDateTime.of(2023, 9,24,14,20));
        scheduleRegistry2.setBreakPeriod(breakPeriodList);

        scheduleRegistryList.add(scheduleRegistry2);
    }

    @Test
    public void testViewAppointmentWhenValidIdThenReturnAppointmentForm() {
        String appointmentId = "1";
        AppointmentForm mockAppointmentForm = new AppointmentForm();
        when(appointmentRepository.findById(anyString())).thenReturn(Optional.of(mockAppointmentForm));

        AppointmentForm returnedAppointmentForm = appointmentService.viewAppointment(appointmentId);

        assertEquals(mockAppointmentForm, returnedAppointmentForm, "The returned appointment form should be the same as the mock appointment form");
    }

    @Test
    public void testGetDoctorById() {
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

    @Test
    public void testGetAvailableDoctorsWhenValidDateThenReturnDoctorList() {
        LocalDate date = LocalDate.now();
        List<DoctorRegistry> doctorRegistries = new ArrayList<>();
        DoctorRegistry doctorRegistry = new DoctorRegistry();
        doctorRegistry.setDoctorEmail("doctor@example.com");
        doctorRegistry.setScheduleRegistries(scheduleRegistryList);
        doctorRegistries.add(doctorRegistry);
        when(doctorRegistryRepository.findAll()).thenReturn(doctorRegistries);

        List<String> returnedDoctorList = appointmentService.getAvailableDoctors(date);
        log.info("returnedDoctorList: {}", returnedDoctorList);
        log.info("schedule registry list: {}", scheduleRegistryList);

        assertThat(returnedDoctorList).isNotEmpty();
    }

    @Test
    public void testGetAvailableDoctorsWhenInvalidDateThenReturnEmptyList() {
        LocalDate date = LocalDate.now().plusDays(1);
        List<DoctorRegistry> doctorRegistries = new ArrayList<>();
        DoctorRegistry doctorRegistry = new DoctorRegistry();
        doctorRegistry.setDoctorEmail("doctor@example.com");
        doctorRegistry.setScheduleRegistries(new ArrayList<>());
        doctorRegistries.add(doctorRegistry);
        when(doctorRegistryRepository.findAll()).thenReturn(doctorRegistries);

        List<String> returnedDoctorList = appointmentService.getAvailableDoctors(date);

        assertThat(returnedDoctorList).isEmpty();
    }

    @Test
    public void testGetAvailableDoctorsWhenNoDoctorRegistriesThenReturnEmptyList() {
        LocalDate date = LocalDate.now();
        when(doctorRegistryRepository.findAll()).thenReturn(new ArrayList<>());

        List<String> returnedDoctorList = appointmentService.getAvailableDoctors(date);

        assertThat(returnedDoctorList).isEmpty();
    }
}