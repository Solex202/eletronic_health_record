package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import freemarker.template.TemplateException;

import java.io.IOException;

public interface DoctorService {
    ApiResponse<?> saveDoctor(Doctor doctor) throws IOException, TemplateException;
}
