package com.example.batch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JobScheduler {

    private final JobLauncher jobLauncher;
    private final JobLocator jobLocator;
    private final Logger logger = LoggerFactory.getLogger(JobScheduler.class);

    public JobScheduler(JobLauncher jobLauncher, JobLocator jobLocator) {
        this.jobLauncher = jobLauncher;
        this.jobLocator = jobLocator;
    }

    @Scheduled(cron = "${cron.parseToXmlChunkJob}")
    public void runParseToXmlChunkJob() throws Exception {
        logger.info("Triggering parseToXmlChunkJob");
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        Job job = jobLocator.getJob("parseToXmlChunkJob");
        jobLauncher.run(job, params);
    }

    @Scheduled(cron = "${cron.parseOrderToXmlJob}")
    public void runParseOrderToXmlJob() throws Exception {
        logger.info("Triggering parseOrderToXmlJob");
        JobParameters params = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        Job job = jobLocator.getJob("parseOrderToXmlJob");
        jobLauncher.run(job, params);
    }
}
