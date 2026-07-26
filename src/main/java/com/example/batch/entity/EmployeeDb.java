package com.example.batch.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class EmployeeDb {
  @Id private int id;
  private String firstName;
  private String lastName;
  private String email;
  private int age;
  private String city;
  private double salary;
  private String joined;
}
