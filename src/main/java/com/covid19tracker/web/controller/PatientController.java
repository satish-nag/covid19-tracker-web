package com.covid19tracker.web.controller;

import com.covid19tracker.web.entities.PatientEntity;
import com.covid19tracker.web.models.GenericResponse;
import com.covid19tracker.web.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("/patient")
@CrossOrigin
public class PatientController {

    @Autowired
    PatientService patientService;

    @PostMapping("/registerTest")
    public ResponseEntity<GenericResponse<PatientEntity>> registerTest(@Valid @RequestBody PatientEntity patientEntity){
        PatientEntity patientEntity1 = patientService.registerTest(patientEntity);
        return ResponseEntity.ok(new GenericResponse<>(patientEntity1,null));
    }

    @GetMapping("/getOtp")
    public ResponseEntity<GenericResponse<String>> getOtp(@RequestParam String mobileNumber){
        return patientService.getOtp(mobileNumber);
    }

    @GetMapping("/searchForPatient")
    public ResponseEntity<GenericResponse<List<PatientEntity>>> searchForPatient(@RequestParam String mobileOrEmail, @RequestParam String otp){
        return patientService.searchForPatient(mobileOrEmail,otp);
    }

}
