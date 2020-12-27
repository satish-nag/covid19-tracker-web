package com.covid19tracker.web.services;

import com.covid19tracker.web.entities.PatientEntity;
import com.covid19tracker.web.models.GenericResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PatientService {
    PatientEntity registerTest(PatientEntity patientEntity);

    ResponseEntity getOtp(String mobileNumber);

    ResponseEntity<GenericResponse<List<PatientEntity>>> searchForPatient(String mobileOrEmail, String otp);
}
