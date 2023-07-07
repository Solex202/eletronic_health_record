package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.dtos.response.ApiResponse;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Doctor;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.DoctorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class DoctorServiceImplementation implements DoctorService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public ApiResponse<?> saveDoctor(Doctor doctor) {

        Doctor newDoctor = doctorRepository.save(doctor);

        return ApiResponse.builder().message("Successful").data(newDoctor).build();
    }
}
