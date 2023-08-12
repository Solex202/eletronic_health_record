package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.RegisteredScheduleResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.BreakPeriod;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.DoctorRegistry;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.ScheduleRegistry;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRegistryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DoctorRegistryServiceImpl implements DoctorRegistryService {
    @Autowired
    private DoctorRegistryRepository doctorRegistryRepository;
    @Override
    public ApiResponse<?> createSchedule(DoctorRegistry doctorRegistry) {

        List<ScheduleRegistry> scheduleRegistries = doctorRegistry.getScheduleRegistries();

        DoctorRegistry registry = new DoctorRegistry();

        for (ScheduleRegistry scheduleRegistry : scheduleRegistries) {
            List<LocalTime> intervals = new ArrayList<>();

            LocalDateTime from = scheduleRegistry.getFrom();
            LocalDateTime to = scheduleRegistry.getTo();
            LocalDate date = from.toLocalDate();

            List<BreakPeriod> breakPeriods = scheduleRegistry.getBreakPeriod();

            log.info("BREAK PERIODS {}",breakPeriods);

            while (from.isBefore(to)) {
                if (!isWithinBreakPeriods(from, breakPeriods)) {
                    intervals.add(LocalTime.of(from.getHour(), from.getMinute()));
                }
                from = from.plusMinutes(30);
            }
            Map<String, List<LocalTime>> m = new HashMap<>();
            m.put(date.toString(), intervals);

            if(registry.getThirtyMinutesInterval()!= null && !registry.getThirtyMinutesInterval().containsKey(date.toString())){
                registry.getThirtyMinutesInterval().put(date.toString(), intervals);
            }
            else registry.setThirtyMinutesInterval(m);

        }
        registry.setDoctorId(doctorRegistry.getDoctorId());
        registry.setDoctorEmail(doctorRegistry.getDoctorEmail());
        registry.setCreatedDate(LocalDateTime.now());
        registry.setModifiedDate(LocalDateTime.now());
        registry.setScheduleRegistries(scheduleRegistries);

        doctorRegistryRepository.save(registry);

        RegisteredScheduleResponse response =RegisteredScheduleResponse.builder()
                .doctorId(doctorRegistry.getDoctorId())
                .doctorEmail(doctorRegistry.getDoctorEmail())
//                .date(d)
                .build();

        return ApiResponse.builder().data(response).build();
    }

    private boolean isWithinBreakPeriods(LocalDateTime from, List<BreakPeriod> breakPeriods) {
        for (BreakPeriod breakPeriod : breakPeriods) {
            if (from.isAfter(breakPeriod.getFrom()) && from.isBefore(breakPeriod.getTo())) {
                return true;
            }
        }
        return false;
    }
}
