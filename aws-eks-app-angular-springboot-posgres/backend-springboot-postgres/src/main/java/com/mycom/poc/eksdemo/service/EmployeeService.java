package com.mycom.poc.eksdemo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mycom.poc.eksdemo.dto.EmployeeDTO;
import com.mycom.poc.eksdemo.model.Employee;
import com.mycom.poc.eksdemo.repository.EmployeeRepository;

@Component
public class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;

	public Iterable<Employee> findAll() {
		return employeeRepository.findAll();
	}

	public Optional<Employee> findByID(Long id) {
		return employeeRepository.findById(id);
	}

	public Employee save(EmployeeDTO employeedto) {
		Employee employee = new Employee(employeedto.getName(), employeedto.getDesignation(), employeedto.getSalary());
		return employeeRepository.save(employee);
	}

	public Employee update(Long empid, EmployeeDTO employeedto) {
		Employee employee = employeeRepository.findById(empid).orElse(null);
		employee.setName(employeedto.getName());
		employee.setDesignation(employeedto.getDesignation());
		employee.setSalary(employeedto.getSalary());
		return employeeRepository.save(employee);
	}

	public void delete(Long empId) {
		Employee employee = new Employee();
		employee.setEmpId(empId);
		employeeRepository.delete(employee);
	}
}
