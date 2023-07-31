package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.BreakPeriod;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.DoctorRegistry;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.ScheduleRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DoctorRegistryServiceImplTest {

    @Autowired
    private DoctorRegistryService doctorRegistryService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testThatDoctorCanProvideAvailableTimeForWeeklySchedule(){
        List<BreakPeriod> breakPeriodList = new ArrayList<>();
        BreakPeriod breakPeriod1 = new BreakPeriod();
        breakPeriod1.setFrom(LocalTime.of(9, 0));
        breakPeriod1.setTo(LocalTime.of(10, 0));

        breakPeriodList.add(breakPeriod1);

        BreakPeriod breakPeriod2 = new BreakPeriod();
        breakPeriod2.setFrom(LocalTime.of(13, 0));
        breakPeriod2.setTo(LocalTime.of(14, 0));

        breakPeriodList.add(breakPeriod2);


        List<ScheduleRegistry> scheduleRegistryList = new ArrayList<>();
        ScheduleRegistry scheduleRegistry1 = new ScheduleRegistry();
        scheduleRegistry1.setFrom(LocalDateTime.of(2023,9,23,9,0));
        scheduleRegistry1.setTo(LocalDateTime.of(2023, 9,23,17,20));
        scheduleRegistry1.setBreakPeriod(breakPeriodList);

        scheduleRegistryList.add(scheduleRegistry1);

        ScheduleRegistry scheduleRegistry2 = new ScheduleRegistry();
        scheduleRegistry2.setFrom(LocalDateTime.of(2023,9,24,11,0));
        scheduleRegistry2.setTo(LocalDateTime.of(2023, 9,24,14,20));
        scheduleRegistry2.setBreakPeriod(breakPeriodList);

        scheduleRegistryList.add(scheduleRegistry2);

        DoctorRegistry doctorRegistry = new DoctorRegistry();
        doctorRegistry.setDoctorId("1uweaiG");
        doctorRegistry.setDoctorEmail("doctors@gmail.com");
        doctorRegistry.setScheduleRegistries(scheduleRegistryList);

        ApiResponse<?> response = doctorRegistryService.createSchedule(doctorRegistry);

    }

    @AfterEach
    void tearDown() {
    }
}