package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.RegisteredScheduleResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.DoctorRegistry;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.ScheduleRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
public class DoctorRegistryServiceImpl implements DoctorRegistryService {
    @Override
    public ApiResponse<?> createSchedule(DoctorRegistry doctorRegistry) {

        List<ScheduleRegistry> scheduleRegistries = doctorRegistry.getScheduleRegistries();

        for (ScheduleRegistry scheduleRegistry : scheduleRegistries) {
            LocalDateTime from = scheduleRegistry.getFrom();
            LocalDateTime to = scheduleRegistry.getTo();
            LocalDate date = from.toLocalDate();

            List<LocalTime> intervals = new ArrayList<>();
            int minutesToAdd = 30;
//            from = from.plusMinutes(minutesToAdd);
            while (!from.isAfter(to)){
                intervals.add(from.toLocalTime());
                from = from.plusMinutes(minutesToAdd);
            }

            log.info("INTERVALS-----> {}", intervals);
            log.info("DATE ----> {}", date);

            int fromHr= from.getHour();
            int toHr= to.getHour();

            log.info("FROM HR ----> {}", fromHr);
            log.info("TO HR ----> {}", toHr);

        }

        RegisteredScheduleResponse response =RegisteredScheduleResponse.builder()
                .doctorId(doctorRegistry.getDoctorId())
                .doctorEmail(doctorRegistry.getDoctorEmail())
                .build();

        return null;
    }
}
