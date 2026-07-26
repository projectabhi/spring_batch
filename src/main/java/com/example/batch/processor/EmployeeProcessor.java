package com.example.batch.processor;

import com.example.batch.entity.EmployeeDb;
import com.example.batch.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmployeeProcessor implements ItemProcessor<Employee, EmployeeDb> {

  @Override
  public EmployeeDb process(Employee employee) throws Exception {
    EmployeeDb employeeDb = new EmployeeDb();
    employeeDb.setId(employee.getId());
    employeeDb.setFirstName(employee.getFirstName());
    employeeDb.setLastName(employee.getLastName());
    employeeDb.setEmail(employee.getEmail());
    employeeDb.setAge(employee.getAge());
    employeeDb.setCity(employee.getCity());
    employeeDb.setSalary(employee.getSalary());
    employeeDb.setJoined(employee.getJoined());
    return employeeDb;
  }
}
