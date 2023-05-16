package com.chicmic.eNaukri.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;
    private String skillName;

    @OneToMany(mappedBy = "skills",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<UserSkills> userSkillsList=new ArrayList<>();

}
