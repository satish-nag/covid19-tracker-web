package com.covid19tracker.web.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Users {
    @Id
    private String username;
    private String password;
    private int enabled;

    @ManyToOne
    @JoinColumn(name = "hospitalCode")
    private Hospital hospital;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private Authorities authorities;
}
