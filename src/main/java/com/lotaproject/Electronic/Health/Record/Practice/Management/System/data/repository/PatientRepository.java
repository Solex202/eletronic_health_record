package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PatientRepository extends MongoRepository<Patient, String> {

}
