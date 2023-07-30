package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.DoctorRegistry;

public interface DoctorRegistryService {
    ApiResponse<?> createSchedule(DoctorRegistry doctorRegistry);
}
