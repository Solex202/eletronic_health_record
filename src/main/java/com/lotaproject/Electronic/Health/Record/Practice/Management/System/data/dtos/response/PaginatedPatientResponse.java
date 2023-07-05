package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PaginatedPatientResponse {

    private List<Patient> patients;

    private int currentPage;

    private int noOfPatients;

    private int pageSize;

}
