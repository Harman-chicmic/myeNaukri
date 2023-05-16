package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Data
@Builder
@RequiredArgsConstructor
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userTokenId;

    private String token;
    @ManyToOne
    @JsonIgnore
    private Users userr;
}
