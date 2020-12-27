package com.covid19tracker.web.controller;

import com.covid19tracker.web.entities.PatientEntity;
import com.covid19tracker.web.models.GenericResponse;
import com.covid19tracker.web.repositories.PatientRepository;
import com.covid19tracker.web.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/searchForPatient")
    @PreAuthorize("hasRole('lab_tech')")
    public ResponseEntity<GenericResponse<Object>> patientEntityList(@RequestParam String id){
        return adminService.patientEntityList(id);
    }

    @PostMapping("/updateStatus")
    @PreAuthorize("hasRole('lab_tech')")
    public ResponseEntity<GenericResponse<List<PatientEntity>>> updateStatus(@RequestBody List<PatientEntity> patientEntities){
        return adminService.updateStatus(patientEntities);
    }
}
