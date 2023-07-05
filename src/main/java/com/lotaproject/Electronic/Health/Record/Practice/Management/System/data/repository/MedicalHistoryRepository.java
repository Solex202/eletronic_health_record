package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.MedicalHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MedicalHistoryRepository extends MongoRepository<MedicalHistory, String> {



}
