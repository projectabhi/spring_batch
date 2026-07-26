package com.example.batch.config;

import com.example.batch.model.Detail;
import com.example.batch.tasklet.FinalizeXmlTasklet;
import com.example.batch.tasklet.OrderParseAndWriteXmlTasklet;
import com.example.batch.tasklet.ParseAndWriteXmlTasklet;
import com.example.batch.tasklet.PreParseTasklet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@Slf4j
public class BatchConfig {

  @Bean
  public Job parseToXmlJob(JobRepository jobRepository, Step parseToXmlStep) {
    return new JobBuilder("parseToXmlJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(parseToXmlStep)
        .build();
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
  public Job parseOrderToXmlJob(JobRepository jobRepository, Step parseOrderToXmlStep) {
    return new JobBuilder("parseOrderToXmlJob", jobRepository)
        .incrementer(new RunIdIncrementer())
        .start(parseOrderToXmlStep)
        .build();
  }

  @Bean
  public Step parseOrderToXmlStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      OrderParseAndWriteXmlTasklet tasklet) {
    return new StepBuilder("parseOrderToXmlStep", jobRepository)
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
        .incrementer(new RunIdIncrementer())
        .start(preParseStep)
        .next(detailChunkStep)
        .next(finalizeXmlStep)
        .build();
  }
}
