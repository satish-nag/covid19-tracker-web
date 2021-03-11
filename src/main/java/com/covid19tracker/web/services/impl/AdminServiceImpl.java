package com.covid19tracker.web.services.impl;

import com.covid19tracker.web.entities.Authorities;
import com.covid19tracker.web.entities.Hospital;
import com.covid19tracker.web.entities.Patient;
import com.covid19tracker.web.entities.Users;
import com.covid19tracker.web.models.GenericResponse;
import com.covid19tracker.web.repositories.HospitalRepository;
import com.covid19tracker.web.repositories.PatientRepository;
import com.covid19tracker.web.repositories.UserRepository;
import com.covid19tracker.web.services.AdminService;
import com.covid19tracker.web.utils.AWSHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    PatientRepository patientRepository;

    @Autowired
    HospitalRepository hospitalRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AWSHelper awsHelper;

    @Value("${mobile.countryCode}")
    private String countryCode;

    @Override
    public ResponseEntity<GenericResponse<Object>> patientEntityList(String id) {
        Hospital hospital = userRepository.findById(SecurityContextHolder.getContext().getAuthentication().getName()).get().getHospital();
        List<Patient> patientList = patientRepository.getPatientEntitiesByApplicationIdOrEmailOrMobileNumberAndHospital(id, id, id,hospital);
        GenericResponse<Object> objectGenericResponse;
        if(patientList ==null || patientList.isEmpty()){
            Map<String,String> errors = new HashMap<>();
            errors.put("generic_error","No Records exist for the given reference number:"+id);
            objectGenericResponse = new GenericResponse<>(null, errors);
            return ResponseEntity.badRequest().body(objectGenericResponse);
        }else {
            objectGenericResponse = new GenericResponse<>(patientList, null);
            return ResponseEntity.ok(objectGenericResponse);
        }
    }

    @Override
    public ResponseEntity<GenericResponse<List<Patient>>> updateStatus(List<Patient> patientEntities) {
        List<Patient> patientList = new ArrayList<>();
        patientRepository.saveAll(patientEntities).forEach(patientList::add);
        if(!patientList.isEmpty()){
            patientList.stream().forEach(patientEntity -> {
                awsHelper.sendSMS(countryCode+patientEntity.getMobileNumber(),"Your COVID test has been completed and result is "+patientEntity.getStatus()+" for application "+patientEntity.getApplicationId()+" tested on "+patientEntity.getCreatedTime());
                awsHelper.sendEmail("saatish.naga@gmail.com",patientEntity.getEmail(),null,null,"Hi "+patientEntity.getName()+"<br/><br/> Your COVID test has been completed and result is "+patientEntity.getStatus()+" for application "+patientEntity.getApplicationId()+"tested on "+patientEntity.getCreatedTime()+"<br/><br/>Regards,<br/>Team Covid Tracker","Covid19 Result for Application ID"+patientEntity.getApplicationId());
            });
        }
        return ResponseEntity.ok(new GenericResponse<>(patientList,null));
    }

    @Override
    public ResponseEntity<GenericResponse<List<Hospital>>> getHospitalList() {
        List<Hospital> hospitals = new ArrayList<>();
        hospitalRepository.findAll().forEach(hospitals::add);
        return ResponseEntity.ok(new GenericResponse<>(hospitals,null));
    }

    @Override
    public ResponseEntity<GenericResponse<String>> changePassword(Users users) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Users user = userRepository.findById(username).get();
        user.setPassword(new BCryptPasswordEncoder().encode(new String(Base64.getDecoder().decode(users.getPassword()))));
        userRepository.save(user);
        return ResponseEntity.ok(new GenericResponse<>("Password Updated Successfully",null));
    }

    @Override
    public ResponseEntity<GenericResponse<String>> createUser(Users users) {
        if(!userRepository.existsUsersByUsernameAndAndHospital_HospitalCode(users.getUsername(),users.getHospital().getHospitalCode())) {
            users.setPassword(new BCryptPasswordEncoder().encode(users.getPassword()));
            users.setEnabled(1);
            Authorities authorities = new Authorities();
            authorities.setUser(users);
            authorities.setUsername(users.getUsername());
            authorities.setAuthority("lab_tech");
            users.setAuthorities(authorities);
            userRepository.save(users);
            return ResponseEntity.ok(new GenericResponse<>("password Changed Successfully", null));
        }else {
            Map<String,String> errors = new HashMap<>();
            errors.put("generic_error","User already exists for the hospital !!");
            return ResponseEntity.badRequest().body(new GenericResponse<>(null,errors));
        }
    }

    @Override
    public ResponseEntity<GenericResponse<String>> addHospital(Hospital hospital) {
        boolean present = hospitalRepository.findById(hospital.getHospitalCode()).isPresent();
        GenericResponse<String> response = new GenericResponse<>();
        if(!present){
            hospitalRepository.save(hospital);
            response.setResponse("Hospital added successfully");
            return ResponseEntity.ok(response);
        }

        Map<String,String> errors = new HashMap<>();
        errors.put("generic_error", "Hospital code already Exists !!!");
        response.setErrors(errors);
        return ResponseEntity.badRequest().body(response);
    }

}
