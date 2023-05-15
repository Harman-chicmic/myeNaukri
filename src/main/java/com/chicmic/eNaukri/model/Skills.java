package com.chicmic.eNaukri.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long skillId;
    private String skillName;

}
