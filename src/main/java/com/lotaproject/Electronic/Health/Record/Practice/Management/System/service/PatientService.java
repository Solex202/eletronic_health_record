package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.RegisterPatientRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.request.UpdatePatientDetailRequest;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;

public interface PatientService {
    ApiResponse<?> registerPatient(RegisterPatientRequest request);

    Patient findByEmail(String email);

    Patient findById(String id);

    ApiResponse<?> updatePatientDetails(String id, UpdatePatientDetailRequest request1);
}
