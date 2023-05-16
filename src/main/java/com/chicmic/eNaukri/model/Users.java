package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@RequiredArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userId;

    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private String currentCompany;
    private String cvPath;
    private String bio;
    private String ppPath;
    private String otp;
    @UuidGenerator
    private String uuid;
    private String link;
//    private Set<Skills> skillsSet=new HashSet<>();

//mappings
    @OneToMany(mappedBy = "companyUsers", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserCompany> userCompanyList=new ArrayList<>();

    @OneToOne(mappedBy = "userLinks", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonIgnore
    private SocialLink socialLink;

    @OneToMany(mappedBy = "edUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Education> educationList=new ArrayList<>();

    @OneToMany(mappedBy = "applicantId", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Application> applicationList=new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch =FetchType.LAZY)
    private List<UserSkills> userSkillsList=new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,fetch=FetchType.EAGER,mappedBy = "userr")
    @JsonIgnore
    private Set<UserToken> userTokenSet=new HashSet<>();

}
