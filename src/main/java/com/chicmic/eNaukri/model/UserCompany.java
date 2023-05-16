package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@RequiredArgsConstructor
public class UserCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userCompanyId;

    private String employeeRole;
    private String roleDesc;
    private boolean currentlyWorking;
    @JsonFormat(pattern="yyyy/mm/dd")
    private LocalDate joinedAt;
    @JsonFormat(pattern="yyyy/mm/dd")
    private LocalDate leftAt;

    //mappings
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Company usersCompany;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    private Users companyUsers;
}
