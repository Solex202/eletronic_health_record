package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.LoginResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.UpdatePatientDetailRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.PaginatedPatientResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;

public interface PatientService {
    ApiResponse<?> registerPatient(RegisterPatientRequest request) throws IOException, TemplateException;

    Patient findByEmail(String email);

    Patient findById(String id);

    ApiResponse<?> updatePatientDetails(String id, UpdatePatientDetailRequest request1);

    PaginatedPatientResponse findByName(int pageNumber, int pageSize,String name);

    List<Patient> findAllPatients();
}
