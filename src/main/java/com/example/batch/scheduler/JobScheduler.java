package com.example.batch.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JobScheduler {

  private final JobLauncher jobLauncher;
  private final JobLocator jobLocator;

  public JobScheduler(JobLauncher jobLauncher, JobLocator jobLocator) {
    this.jobLauncher = jobLauncher;
    this.jobLocator = jobLocator;
  }

  @Scheduled(cron = "${cron.parseToXmlChunkJob}")
  public void runParseToXmlChunkJob() throws Exception {
    log.info("Triggering parseToXmlChunkJob");
    JobParameters params =
        new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
    Job job = jobLocator.getJob("parseToXmlChunkJob");
    jobLauncher.run(job, params);
  }

  //    @Scheduled(cron = "${cron.parseOrderToXmlJob}")
  //    public void runParseOrderToXmlJob() throws Exception {
  //        logger.info("Triggering parseOrderToXmlJob");
  //        JobParameters params = new JobParametersBuilder()
  //                .addLong("time", System.currentTimeMillis())
  //                .toJobParameters();
  //        Job job = jobLocator.getJob("parseOrderToXmlJob");
  //        jobLauncher.run(job, params);
  //    }
}
