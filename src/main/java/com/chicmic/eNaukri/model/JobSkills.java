package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class JobSkills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobSkillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Skills jobSkill;
}
