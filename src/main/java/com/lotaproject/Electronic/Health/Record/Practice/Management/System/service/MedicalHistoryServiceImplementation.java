package com.lotaproject.Electronic.Health.Record.Practice.Management.System.service;

import  com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.MedicalHistory;
import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository.MedicalHistoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MedicalHistoryServiceImplementation implements MedicalHistoryService {

    @Autowired
    private MedicalHistoryRepository medicalHistoryRepository;


    @Override
    public void createMedicalHistory(MedicalHistory medicalHistory) {
        medicalHistoryRepository.save(medicalHistory);
    }
}

