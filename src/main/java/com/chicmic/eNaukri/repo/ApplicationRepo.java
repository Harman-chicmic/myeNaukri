package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepo extends JpaRepository<Application,Long> {
}
