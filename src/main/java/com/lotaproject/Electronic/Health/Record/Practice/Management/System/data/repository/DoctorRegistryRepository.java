package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.DoctorRegistry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorRegistryRepository extends MongoRepository<DoctorRegistry, String> {
    DoctorRegistry findByDoctorEmail(String doctorName);
}
