package com.covid19tracker.web.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
@Getter
@Setter
public class PatientEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid",strategy = "org.hibernate.id.UUIDGenerator")
    private String applicationId;

    @NotEmpty(message = "Name should not be empty")
    private String name;

    private String countryCode;

    @NotEmpty(message = "Mobile Number should not be empty")
    @Size(min = 10,max = 10,message = "Mobile Number should be of 10 digits")
    @Pattern(regexp = "[0-9]{10}",message = "Mobile Number contain only digits")
    private String mobileNumber;

    @NotEmpty(message = "gender should not be empty")
    private String gender;

    @NotNull(message = "Age should not be empty")
    @Range(min = 0,max = 99,message = "Age should be between 0 and 99")
    private int age;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email address is not valid")
    private String email;

    private String status;

    @CreationTimestamp
    private Timestamp createdTime;

    @UpdateTimestamp
    private Timestamp updatedTime;

    private String otp;

    @Temporal(TemporalType.TIMESTAMP)
    private Calendar otpExpiresIn;


}
