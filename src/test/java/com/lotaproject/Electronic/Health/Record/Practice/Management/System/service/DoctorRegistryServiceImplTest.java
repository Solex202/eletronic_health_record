package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.RegisteredScheduleResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.DoctorRegistry;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.ScheduleRegistry;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRegistryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorRegistryServiceImplTest {

    @Mock
    private DoctorRegistryRepository doctorRegistryRepository;

    @InjectMocks
    private DoctorRegistryServiceImpl doctorRegistryService;

    private DoctorRegistry doctorRegistry;
    private List<ScheduleRegistry> scheduleRegistries;

    @BeforeEach
    public void setUp() {
        scheduleRegistries = new ArrayList<>();
        scheduleRegistries.add(new ScheduleRegistry("1", LocalDateTime.now(), LocalDateTime.now().plusHours(2), new ArrayList<>()));

        doctorRegistry = new DoctorRegistry();
        doctorRegistry.setDoctorId("1");
        doctorRegistry.setDoctorEmail("doctor1@gmail.com");
        doctorRegistry.setScheduleRegistries(scheduleRegistries);
    }

    @Test
    public void testCreateScheduleWhenValidDoctorRegistryThenScheduleCreated() {
        when(doctorRegistryRepository.save(any(DoctorRegistry.class))).thenReturn(doctorRegistry);

        ApiResponse<?> response = doctorRegistryService.createSchedule(doctorRegistry);

        assertThat(response.getMessage()).isEqualTo("Schedule created successfully");
        assertThat(response.getData()).isInstanceOf(RegisteredScheduleResponse.class);
        verify(doctorRegistryRepository, times(1)).save(any(DoctorRegistry.class));
    }

    @Test
    public void testCreateScheduleWhenNullDoctorRegistryThenError() {
        ApiResponse<?> response = doctorRegistryService.createSchedule(null);

        assertThat(response.getMessage()).isEqualTo("Doctor registry cannot be null");
    }

    @Test
    public void testCreateScheduleWhenNoScheduleRegistriesThenError() {
        doctorRegistry.setScheduleRegistries(new ArrayList<>());

        ApiResponse<?> response = doctorRegistryService.createSchedule(doctorRegistry);

        assertThat(response.getMessage()).isEqualTo("No schedule registries found");
    }
}