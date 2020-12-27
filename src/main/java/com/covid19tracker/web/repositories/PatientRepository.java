package com.covid19tracker.web.repositories;

import com.covid19tracker.web.entities.PatientEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<PatientEntity,String> {
    List<PatientEntity> getPatientEntitiesByApplicationIdOrEmailOrMobileNumber(String application,String email,String mob);
}
