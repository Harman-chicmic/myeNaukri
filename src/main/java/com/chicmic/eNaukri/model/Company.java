package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long companyId;

    private String companyName;
    private String locatedAt;
    private LocalDate foundedIn;
    private String about;
    private String ppPath;

    @OneToMany(mappedBy = "postFor", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Job> jobList=new ArrayList<>();

    @OneToMany(mappedBy = "exCompany", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Experience> experienceList=new ArrayList<>();

    @OneToOne(mappedBy = "companyLinks",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private SocialLink socialLink;
}
