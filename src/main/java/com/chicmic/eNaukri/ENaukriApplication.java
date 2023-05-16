package com.chicmic.eNaukri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
public class ENaukriApplication {

	public static void main(String[] args) {
		SpringApplication.run(ENaukriApplication.class, args);
	}

	@Bean
	public Executor executor(){
		ThreadPoolTaskExecutor taskExecutor=new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(3);
		taskExecutor.setMaxPoolSize(3);
		taskExecutor.setQueueCapacity(700);
		taskExecutor.initialize();
		return taskExecutor;
	}

}
