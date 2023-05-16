package com.chicmic.eNaukri.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@RequiredArgsConstructor
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long fpId;
    private String token;
    @ManyToOne
    private Users fUser;
    private LocalDateTime expiryTime;

    public boolean isExpired(){
        return this.expiryTime.isBefore(LocalDateTime.now());
    }
}
