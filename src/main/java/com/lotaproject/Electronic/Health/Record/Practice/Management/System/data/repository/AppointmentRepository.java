package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.AppointmentForm;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppointmentRepository extends MongoRepository<AppointmentForm, String> {

}
