package com.chicmic.eNaukri.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class SocialLink {

    @Id
    @GeneratedValue
    private Long socialLinkId;

    private String linkedIn;
    private String twitter;
    private String others;
    private String website;

    @OneToOne
    private Company companyLinks;
    @OneToOne
    private Users userLinks;
}
