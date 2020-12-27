package com.covid19tracker.web.services.impl;

import com.covid19tracker.web.entities.PatientEntity;
import com.covid19tracker.web.models.GenericResponse;
import com.covid19tracker.web.repositories.PatientRepository;
import com.covid19tracker.web.services.AdminService;
import com.covid19tracker.web.utils.AWSHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    PatientRepository patientRepository;

    @Autowired
    AWSHelper awsHelper;

    @Override
    public ResponseEntity<GenericResponse<Object>> patientEntityList(String id) {
        List<PatientEntity> patientEntityList = patientRepository.getPatientEntitiesByApplicationIdOrEmailOrMobileNumber(id, id, id);
        GenericResponse<Object> objectGenericResponse;
        if(patientEntityList==null || patientEntityList.isEmpty()){
            Map<String,String> errors = new HashMap<>();
            errors.put("generic_error","No Records exist for the given reference number:"+id);
            objectGenericResponse = new GenericResponse<>(null, errors);
            return ResponseEntity.badRequest().body(objectGenericResponse);
        }else {
            objectGenericResponse = new GenericResponse<>(patientEntityList, null);
            return ResponseEntity.ok(objectGenericResponse);
        }
    }

    @Override
    public ResponseEntity<GenericResponse<List<PatientEntity>>> updateStatus(List<PatientEntity> patientEntities) {
        List<PatientEntity> patientEntityList = new ArrayList<>();
        patientRepository.saveAll(patientEntities).forEach(patientEntityList::add);
        if(!patientEntityList.isEmpty()){
            patientEntityList.stream().forEach(patientEntity -> {
                awsHelper.sendSMS(patientEntity.getCountryCode()+patientEntity.getMobileNumber(),"Your COVID test has been completed and result is "+patientEntity.getStatus()+" for application "+patientEntity.getApplicationId()+" tested on "+patientEntity.getCreatedTime());
                awsHelper.sendEmail("saatish.naga@gmail.com",patientEntity.getEmail(),null,null,"Hi "+patientEntity.getName()+"<br/><br/> Your COVID test has been completed and result is "+patientEntity.getStatus()+" for application "+patientEntity.getApplicationId()+"tested on "+patientEntity.getCreatedTime()+"<br/><br/>Regards,<br/>Team Covid Tracker","Covid19 Result for Application ID"+patientEntity.getApplicationId());
            });
        }
        return ResponseEntity.ok(new GenericResponse<>(patientEntityList,null));
    }
}
