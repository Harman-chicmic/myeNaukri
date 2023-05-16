package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTokenRepo extends JpaRepository<UserToken,Long> {
    UserToken findByUuid(String token);

    void deleteByUuid(String value);
}
