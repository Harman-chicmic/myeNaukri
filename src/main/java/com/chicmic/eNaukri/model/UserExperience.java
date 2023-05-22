package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class UserExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userExperienceId;

    @ManyToOne
    @JsonIgnore
    private Users user;

    @ManyToOne
    @JsonIgnore
    private Experience experience;
}