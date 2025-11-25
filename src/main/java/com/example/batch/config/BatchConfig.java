package com.example.batch.config;

import com.example.batch.model.Detail;
import com.example.batch.tasklet.FinalizeXmlTasklet;
import com.example.batch.tasklet.ParseAndWriteXmlTasklet;
import com.example.batch.tasklet.PreParseTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

  @Bean
  public Job parseToXmlJob(JobRepository jobRepository, Step parseToXmlStep) {
    return new JobBuilder("parseToXmlJob", jobRepository).start(parseToXmlStep).build();
  }

  @Bean
  public Step parseToXmlStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ParseAndWriteXmlTasklet tasklet) {
    return new StepBuilder("parseToXmlStep", jobRepository)
        .tasklet(tasklet, transactionManager)
        .build();
  }

  @Bean
  public Step preParseStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      PreParseTasklet preParseTasklet) {
    return new StepBuilder("preParseStep", jobRepository)
        .tasklet(preParseTasklet, transactionManager)
        .build();
  }

  @Bean
  public Step detailChunkStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      ItemReader<Detail> detailItemReader,
      ItemProcessor<Detail, Detail> detailItemProcessor,
      ItemWriter<Detail> detailItemWriter) {
    return new StepBuilder("detailChunkStep", jobRepository)
        .<Detail, Detail>chunk(50, transactionManager)
        .reader(detailItemReader)
        .processor(detailItemProcessor)
        .writer(detailItemWriter)
        .build();
  }

  @Bean
  public Step finalizeXmlStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      FinalizeXmlTasklet finalizeXmlTasklet) {
    return new StepBuilder("finalizeXmlStep", jobRepository)
        .tasklet(finalizeXmlTasklet, transactionManager)
        .build();
  }

  @Bean
  public Job parseToXmlChunkJob(
      JobRepository jobRepository, Step preParseStep, Step detailChunkStep, Step finalizeXmlStep) {
    return new JobBuilder("parseToXmlChunkJob", jobRepository)
        .start(preParseStep)
        .next(detailChunkStep)
        .next(finalizeXmlStep)
        .build();
  }
}
