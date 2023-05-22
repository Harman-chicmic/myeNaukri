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
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long exId;
    private String universityName;
    private String majors;
    private LocalDate startFrom;
    @JsonFormat(pattern="yyyy/mm/dd")
    private LocalDate endOn;
    @JsonFormat(pattern="yyyy/mm/dd")
    private boolean student;
    private String degree;

    @ManyToOne
    @JsonIgnore
    private Users edUser;
}
