package com.covid19tracker.web.controller;

import com.covid19tracker.web.entities.Hospital;
import com.covid19tracker.web.entities.Patient;
import com.covid19tracker.web.entities.Users;
import com.covid19tracker.web.models.GenericResponse;
import com.covid19tracker.web.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    @Autowired
    AdminService adminService;

    public enum ROLES{
        admin("Admin"),
        lab_tech("Lab Technician");

        private final String role;

        ROLES(String role) {
            this.role = role;
        }

        public String getRole() {
            return role;
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity authenticate(@RequestHeader ROLES role){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(role.name()))) {
            return ResponseEntity.ok().build();
        }
        Map<String,String> errors = new HashMap<>();
        errors.put("generic_error","You dont have the "+role.getRole()+" role  to login");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new GenericResponse<>(null,errors));
    }

    @PostMapping("/searchForPatient")
    @PreAuthorize("hasAuthority('lab_tech')")
    public ResponseEntity<GenericResponse<Object>> patientEntityList(@RequestParam String id){
        return adminService.patientEntityList(id);
    }

    @PostMapping("/updateStatus")
    @PreAuthorize("hasAuthority('lab_tech')")
    public ResponseEntity<GenericResponse<List<Patient>>> updateStatus(@RequestBody List<Patient> patientEntities){
        return adminService.updateStatus(patientEntities);
    }

    @GetMapping("/getHospitalList")
    public ResponseEntity<GenericResponse<List<Hospital>>> getHospitalList(){
        return adminService.getHospitalList();
    }

    @PostMapping("/createUser")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<GenericResponse<String>> createUser(@RequestBody Users users){
        return adminService.createUser(users);
    }

    @PostMapping("/updatePassword")
    @PreAuthorize("hasAnyAuthority('admin','lab_tech')")
    public ResponseEntity<GenericResponse<String>> updatePassword(@RequestBody Users users){
        return adminService.changePassword(users);
    }

    @PostMapping("/addHospital")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<GenericResponse<String>> addHospital(@RequestBody Hospital hospital){
        return adminService.addHospital(hospital);
    }
}
