package com.chicmic.eNaukri.repo;

import com.chicmic.eNaukri.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;



public interface JobRepo extends JpaRepository<Job,Long> {
}
