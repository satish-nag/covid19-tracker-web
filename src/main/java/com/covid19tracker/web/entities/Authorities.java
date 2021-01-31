package com.covid19tracker.web.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@IdClass(Authority.class)
public class Authorities implements Serializable {

    @Id
    private String username;

    @Id
    private String authority;

    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    @MapsId("username")
    @JoinColumn(name = "username")
    private Users user;

}

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Authority implements Serializable{
    private String username;
    private String authority;
}