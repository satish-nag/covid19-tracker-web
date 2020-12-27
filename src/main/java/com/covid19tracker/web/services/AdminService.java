package com.covid19tracker.web.services;

import com.covid19tracker.web.entities.PatientEntity;
import com.covid19tracker.web.models.GenericResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface AdminService {
    ResponseEntity<GenericResponse<Object>> patientEntityList(String id);
    ResponseEntity<GenericResponse<List<PatientEntity>>> updateStatus(List<PatientEntity> patientEntities);
}
