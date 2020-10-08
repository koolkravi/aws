package com.mycom.poc.eksdemo.controller.v2;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mycom.poc.eksdemo.dto.EmployeeDTO;
import com.mycom.poc.eksdemo.model.Employee;
import com.mycom.poc.eksdemo.service.EmployeeService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController("EmployeeController2")
@RequestMapping("/api/v2")
public class EmployeeController {
	static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
	@Autowired
	EmployeeService employeeService;

	@GetMapping(value = "/employees")
	public ResponseEntity<Object> getEmployees() {
		return new ResponseEntity<>(employeeService.findAll(), HttpStatus.OK);
	}

	@GetMapping(value = "/employees/{id}")
	public ResponseEntity<Object> getEmployeeById(@PathVariable Long id) {
		Optional<Employee> employee = employeeService.findByID(id);
		return new ResponseEntity<>(employee, HttpStatus.OK);
	}

	@PostMapping("/employees")
	public ResponseEntity<Object> createEmployee(@RequestBody EmployeeDTO employee) {
		employeeService.save(employee);
		return new ResponseEntity<>("Employee is created successfully", HttpStatus.CREATED);
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<Object> updateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeDTO employee) {
		employeeService.update(id, employee);
		return new ResponseEntity<>("Employee is updated successsfully", HttpStatus.OK);
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Object> deleteEmployee(@PathVariable("id") Long id) {

		employeeService.delete(id);
		return new ResponseEntity<>("Employee is deleted successsfully", HttpStatus.OK);
	}
}
