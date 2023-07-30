package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.DoctorRegistry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorWeeklyRegistryRepository extends MongoRepository<DoctorRegistry, String> {
}
