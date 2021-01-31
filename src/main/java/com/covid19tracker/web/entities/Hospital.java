package com.covid19tracker.web.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Hospital {

    @Id
    private String hospitalCode;
    private String hospitalName;

    @OneToMany(mappedBy = "hospital",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Patient> patientList;

    @OneToMany(mappedBy = "hospital")
    @JsonIgnore
    private List<Users> users;
}
