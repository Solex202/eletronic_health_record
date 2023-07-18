package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.DoctorWeeklyRegistry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DoctorWeeklyRegistryRepository extends MongoRepository<DoctorWeeklyRegistry, String> {
}
