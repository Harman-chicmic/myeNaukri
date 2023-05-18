package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    private String cvPath;
    private boolean priority;
    private boolean favourite;
    @ManyToOne
    @JsonIgnore
    private Users applicantId;

    @ManyToOne
    @JsonIgnore
    private Job jobId;
}
