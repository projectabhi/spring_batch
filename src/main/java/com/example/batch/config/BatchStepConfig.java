package com.example.batch.config;

import com.example.batch.model.Employee;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class BatchStepConfig {

  @Bean
  @StepScope
  public FlatFileItemReader<Employee> reader(
      @Value("#{stepExecutionContext['startingIndex']}") Long startingIndex,
      @Value("#{stepExecutionContext['itemCount']}") Long itemCount,
      @Value("${batch.config.csv-path}") String path) {

    return new FlatFileItemReaderBuilder<Employee>()
        .name("csvSegmentReader")
        .resource(new FileSystemResource(path))
        .delimited()
        .names(
            "id",
            "first_name",
            "last_name",
            "email",
            "age",
            "city",
            "salary",
            "joined") // Replace with actual headers
        .targetType(Employee.class)
        .linesToSkip(startingIndex.intValue()) // Skip up to this partition's starting index
        .maxItemCount(itemCount.intValue()) // Restrict reader to partition budget limit
        .build();
  }
}
