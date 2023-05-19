package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@RequiredArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long expId;
    private String role;
    private String roleDesc;
    private boolean currentlyWorking;
    @JsonFormat(pattern="yyyy/mm/dd")
    private LocalDate joinedOn;
    @JsonFormat(pattern="yyyy/mm/dd")
    private LocalDate endedOn;

    @ManyToOne
    @JsonIgnore
    private Users expUser;

    @ManyToOne
    @JsonIgnore
    private Company exCompany;
}
