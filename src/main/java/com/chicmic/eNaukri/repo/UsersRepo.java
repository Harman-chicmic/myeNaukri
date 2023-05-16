package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository< Users,Long> {
}
