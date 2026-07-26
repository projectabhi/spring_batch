package com.example.batch.repository;

import com.example.batch.entity.EmployeeDb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeDbRepo extends JpaRepository<EmployeeDb, Long> {}
