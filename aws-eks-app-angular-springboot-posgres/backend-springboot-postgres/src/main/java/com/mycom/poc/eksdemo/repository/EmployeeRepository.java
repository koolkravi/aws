package com.mycom.poc.eksdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycom.poc.eksdemo.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
