package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
