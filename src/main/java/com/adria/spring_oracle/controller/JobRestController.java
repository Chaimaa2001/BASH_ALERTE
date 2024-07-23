package com.adria.spring_oracle.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JobRestController {
    private static final Logger logger = LoggerFactory.getLogger(JobRestController.class);
    private final JobLauncher jobLauncher;
    private final Job job;

    @GetMapping("/startJob")
    public BatchStatus load() {
        JobExecution jobExecution = null;
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            jobExecution = jobLauncher.run(job, jobParameters);

            while (jobExecution.isRunning()) {
                logger.info("Job is running...");
                Thread.sleep(1000);  // Pause pour éviter une boucle de CPU intensive
            }

            logger.info("Job finished with status: " + jobExecution.getStatus());
            return jobExecution.getStatus();
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException | InterruptedException e) {
            logger.error("Job failed to start", e);
            if (jobExecution != null) {
                logger.error("Job execution status: " + jobExecution.getStatus());
            }
            return BatchStatus.FAILED;
        }
    }
}
