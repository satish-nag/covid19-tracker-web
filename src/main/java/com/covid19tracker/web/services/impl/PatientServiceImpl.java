package com.covid19tracker.web.services.impl;

import com.covid19tracker.web.entities.Patient;
import com.covid19tracker.web.models.GenericResponse;
import com.covid19tracker.web.repositories.PatientRepository;
import com.covid19tracker.web.services.PatientService;
import com.covid19tracker.web.utils.AWSHelper;
import com.covid19tracker.web.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PatientServiceImpl implements PatientService {

    @Autowired
    PatientRepository patientRepository;

    @Autowired
    AWSHelper awsHelper;

    @Override
    public Patient registerTest(Patient patient) {
        log.info("Registering the patient with name {}, mobile_number {}, email {}", patient.getName(), patient.getMobileNumber(), patient.getEmail());
        Patient patient1 = patientRepository.save(patient);
        //awsHelper.sendSMS(patientEntity.getCountryCode()+patientEntity.getMobileNumber(),"You have successfully registered for the test, and your application id is "+patientEntity1.getApplicationId()+", please save for future reference");
        //awsHelper.sendEmail("saatish.naga@gmail.com",patientEntity.getEmail(),null,null,"Hi "+patientEntity.getName()+",<br/><br/>You have successfully registered for the test, your application id is "+patientEntity1.getApplicationId()+". Please save the application ID for future Reference</p> <br/><br/> Regards,<br/>Team COVID Tracker","Successfully Regersterd for COVID Test");
        log.info("Registration is successful for the patient {}, and application_id is {}", patient1.getName(), patient1.getApplicationId());
        return patient1;
    }

    @Override
    public ResponseEntity<GenericResponse<String>> getOtp(String mobileNumber) {
        List<Patient> patientEntities = patientRepository.getPatientEntitiesByApplicationIdOrEmailOrMobileNumber(mobileNumber, mobileNumber, mobileNumber);
        if(!patientEntities.isEmpty()){
            String otp = CommonUtils.getOtp();
            String countryCode = patientEntities.get(0).getCountryCode();
            if(!countryCode.startsWith("+"))
                countryCode="+"+countryCode;
            boolean isOtpSent = awsHelper.sendSMS(countryCode + mobileNumber, "Your OTP: " + otp + ", your OTP is valid for 10 minutes only");
            boolean isEmailSent = awsHelper.sendEmail("saatish.naga@gmail.com", patientEntities.get(0).getEmail(), null, null, "Hi " + patientEntities.get(0).getName() + ",<br/><br/>Your OTP for checking your application status is " + otp + "your OTP is Valid for only 10 minutes", "OTP to check status");
            if(isOtpSent || isEmailSent) {
                patientEntities.stream().forEach(patientEntity -> {
                    patientEntity.setOtp(otp);
                    patientEntity.setOtpExpiresIn(CommonUtils.getCalendar(Calendar.MINUTE, 10));
                });
                patientRepository.saveAll(patientEntities);
                return ResponseEntity.ok(new GenericResponse("Success", null));
            }else {
                Map<String,String> errors = new HashMap<>();
                GenericResponse genericResponse = new GenericResponse();
                errors.put("generic_error","Unable to sent the OTP to the registered mobile number: "+mobileNumber);
                genericResponse.setErrors(errors);
                return ResponseEntity.badRequest().body(genericResponse);
            }
        }else {
            Map<String,String> errors = new HashMap<>();
            GenericResponse genericResponse = new GenericResponse();
            errors.put("generic_error","Your Number "+mobileNumber+" is not registered");
            genericResponse.setErrors(errors);
            return ResponseEntity.badRequest().body(genericResponse);
        }
    }

    @Override
    public ResponseEntity<GenericResponse<List<Patient>>> searchForPatient(String mobileOrEmail, String otp) {
        List<Patient> patientList = patientRepository.getPatientEntitiesByApplicationIdOrEmailOrMobileNumber(mobileOrEmail, mobileOrEmail, mobileOrEmail)
                .stream().sorted(Comparator.comparing(Patient::getOtpExpiresIn)).collect(Collectors.toList());
        if(patientList !=null && !patientList.isEmpty()){
            Calendar otpExpiresIn = patientList.get(0).getOtpExpiresIn();
            Calendar calendar = Calendar.getInstance();
            if(calendar.compareTo(otpExpiresIn)<=0 && patientList.get(0).getOtp().equalsIgnoreCase(otp)){
                return  ResponseEntity.ok(new GenericResponse<>(patientList,null));
            }else{
                Map<String,String> errors = new HashMap<>();
                errors.put("generic_error","OTP is either wrong or expired");
                return ResponseEntity.badRequest().body(new GenericResponse<>(null,errors));
            }
        }else{
            Map<String,String> errors = new HashMap<>();
            errors.put("generic_error","No Entries present for the given reference number.");
            return ResponseEntity.badRequest().body(new GenericResponse<>(null,errors));
        }
    }
}
