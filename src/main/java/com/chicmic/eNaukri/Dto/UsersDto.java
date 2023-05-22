package com.chicmic.eNaukri.Dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.web.multipart.MultipartFile;

@Data public class UsersDto {

    private String fullName;
    private String email;
    private String phoneNumber;
    private String currentCompany;
    private String bio;
    private String password;
}
