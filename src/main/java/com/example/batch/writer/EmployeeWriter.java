package com.example.batch.writer;

import com.example.batch.entity.EmployeeDb;
import com.example.batch.repository.EmployeeDbRepo;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeWriter implements ItemWriter<EmployeeDb> {

  @Autowired private EmployeeDbRepo employeeDbRepo;

  @Override
  public void write(Chunk<? extends EmployeeDb> chunk) throws Exception {
    employeeDbRepo.saveAll(chunk.getItems());
  }
}
