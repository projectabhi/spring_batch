package com.example.batch.config;

import com.example.batch.entity.EmployeeDb;
import com.example.batch.model.Employee;
import com.example.batch.partitioner.CsvLinePartitioner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class EmployeeJobConfig {

  @Value("${batch.config.csv-path}")
  private String csvPath;

  @Bean
  public Job partitionedCsvJob(JobRepository jobRepository, Step managerStep) {
    return new JobBuilder("partitionedCsvJob", jobRepository).start(managerStep).build();
  }

  // Manager Step: Tracks partitions and spreads execution across thread bounds
  @Bean
  public Step managerStep(JobRepository jobRepository, Step workerStep, TaskExecutor taskExecutor) {
    return new StepBuilder("managerStep", jobRepository)
        .partitioner("workerStep", new CsvLinePartitioner(csvPath))
        .step(workerStep)
        .gridSize(4) // Number of concurrent threads/partitions
        .taskExecutor(taskExecutor)
        .build();
  }

  // Worker Step: Processes the specific subset of data chunks
  @Bean
  public Step workerStep(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      FlatFileItemReader<Employee> reader,
      ItemProcessor<Employee, EmployeeDb> processor,
      ItemWriter<EmployeeDb> writer) {
    return new StepBuilder("workerStep", jobRepository)
        .<Employee, EmployeeDb>chunk(
            1000, transactionManager) // Balance memory vs transaction overhead
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public TaskExecutor taskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(4);
    executor.setMaxPoolSize(8);
    executor.setQueueCapacity(20);
    executor.setThreadNamePrefix("Batch-Worker-");
    executor.initialize();
    return executor;
  }
}
