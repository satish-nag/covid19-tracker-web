package com.covid19tracker.web.services;

import com.covid19tracker.web.entities.Hospital;
import com.covid19tracker.web.entities.Patient;
import com.covid19tracker.web.entities.Users;
import com.covid19tracker.web.models.GenericResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminService {
    ResponseEntity<GenericResponse<Object>> patientEntityList(String id);
    ResponseEntity<GenericResponse<List<Patient>>> updateStatus(List<Patient> patientEntities);

    ResponseEntity<GenericResponse<List<Hospital>>> getHospitalList();

    ResponseEntity<GenericResponse<String>> changePassword(Users users);
    ResponseEntity<GenericResponse<String>> createUser(Users users);
    ResponseEntity<GenericResponse<String>> addHospital(Hospital hospital);
}
