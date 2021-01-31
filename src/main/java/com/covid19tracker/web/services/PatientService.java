package com.covid19tracker.web.services;

import com.covid19tracker.web.entities.Patient;
import com.covid19tracker.web.models.GenericResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PatientService {
    Patient registerTest(Patient patient);

    ResponseEntity getOtp(String mobileNumber);

    ResponseEntity<GenericResponse<List<Patient>>> searchForPatient(String mobileOrEmail, String otp);
}
