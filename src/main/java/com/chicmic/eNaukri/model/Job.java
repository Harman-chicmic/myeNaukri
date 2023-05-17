package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    private String jobTitle;
    private String jobDesc;
    @JsonFormat(pattern="yyyy/mm/dd")
    private LocalDate postedOn;
    @JsonFormat(pattern="yyyy/mm/dd")
    private LocalDate updatedOn;
    @JsonFormat(pattern="yyyy/mm/dd")
    private LocalDate expiresAt;
    private boolean active;

//mappings
    @OneToMany(mappedBy = "jobId", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Application> applicationList =new ArrayList<>();

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JsonIgnore
    private Company postFor;



}
