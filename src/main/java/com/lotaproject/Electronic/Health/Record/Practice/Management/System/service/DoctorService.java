package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    ApiResponse<?> saveDoctor(Doctor doctor) throws IOException, TemplateException;

    List<Doctor> findAllDoctors();

    String deleteDoctorFromDatabase(String doctorId);

    Doctor findDoctorByEmail(String email);

    Doctor findDoctorById(String id);
}
