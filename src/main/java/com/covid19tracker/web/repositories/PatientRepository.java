package com.covid19tracker.web.repositories;

import com.covid19tracker.web.entities.Hospital;
import com.covid19tracker.web.entities.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends CrudRepository<Patient,String> {
    List<Patient> getPatientEntitiesByApplicationIdOrEmailOrMobileNumber(String application, String email, String mob);
    List<Patient> getPatientEntitiesByApplicationIdOrEmailOrMobileNumberAndHospital(String application, String email, String mob, Hospital hospital);
}
