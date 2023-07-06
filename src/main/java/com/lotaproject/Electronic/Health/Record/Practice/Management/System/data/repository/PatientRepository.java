package com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.repository;

import com.lotaproject.Electronic.Health.Record.Practice.Management.System.data.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PatientRepository extends MongoRepository<Patient, String> {

    boolean existsByEmail(String email);

    Optional<Patient> findByEmail(String email);

    Page<Patient> findByFirstNameContaining(String name, Pageable pageable);

    Page<Patient> findByFirstName(String name, Pageable pageable);

    Page<Patient> findByFirstNameAndLastNameContaining(String name, Pageable pageable);

    Page<Patient> findByFirstNameOrLastNameContaining(String name, Pageable pageable);

    Page<Patient> findByFirstNameContainingOrLastNameContaining(String name,String n, Pageable pageable);
}
