package com.project.joblist.JobList;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync(proxyTargetClass = true)
public class JobListApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobListApplication.class, args);
	}

}
