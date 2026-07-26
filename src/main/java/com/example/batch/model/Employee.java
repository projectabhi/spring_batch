package com.example.batch.model;

import lombok.Data;

@Data
public class Employee {
  private int id;
  private String firstName;
  private String lastName;
  private String email;
  private int age;
  private String city;
  private double salary;
  private String joined;
}
