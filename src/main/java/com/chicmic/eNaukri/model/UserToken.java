package com.chicmic.eNaukri.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long userTokenId;

    private String token;
    @ManyToOne
    @JsonIgnore
    private Users userr;
}
