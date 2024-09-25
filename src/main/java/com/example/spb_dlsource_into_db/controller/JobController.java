package com.example.spb_dlsource_into_db.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Jobs")
public class JobController {
    private Job job;
    private JobOperator jobOperator;
    private JobLauncher jobLauncher;

    public JobController(Job job, JobOperator jobOperator, JobLauncher jobLauncher) {
        this.job = job;
        this.jobOperator = jobOperator;
        this.jobLauncher = jobLauncher;
    }
    @GetMapping("/importDLCustomer")
    public String importDLCustomer() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("Start_At", System.currentTimeMillis())
                .toJobParameters();
        try{
            jobLauncher.run(job, jobParameters);
            return"Job start successfully!";
        }catch(Exception e){
            return "Error when start job!" + e.getMessage();
        }
    }
}
