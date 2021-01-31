package com.covid19tracker.web.controller;

import com.covid19tracker.web.entities.Patient;
import com.covid19tracker.web.models.GenericResponse;
import com.covid19tracker.web.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/patient")
@CrossOrigin
public class PatientController {

    @Autowired
    PatientService patientService;

    @PostMapping("/registerTest")
    public ResponseEntity<GenericResponse<Patient>> registerTest(@Valid @RequestBody Patient patient){
        Patient patient1 = patientService.registerTest(patient);
        return ResponseEntity.ok(new GenericResponse<>(patient1,null));
    }

    @GetMapping("/getOtp")
    public ResponseEntity<GenericResponse<String>> getOtp(@RequestParam String mobileNumber){
        return patientService.getOtp(mobileNumber);
    }

    @GetMapping("/searchForPatient")
    public ResponseEntity<GenericResponse<List<Patient>>> searchForPatient(@RequestParam String mobileOrEmail, @RequestParam String otp){
        return patientService.searchForPatient(mobileOrEmail,otp);
    }

}
