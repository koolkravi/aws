package com.mycom.poc.eksdemo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {
	static final Logger log = LoggerFactory.getLogger(EmployeeController.class);
	public static List<EmployeeDTO> employees = empList();

	@GetMapping(value = "/employees")
	public ResponseEntity<Object> getEmployees() {
		return new ResponseEntity<>(empList(), HttpStatus.OK);
	}

	@GetMapping(value = "/employees/{id}")
	public ResponseEntity<Object> getEmployeeById(@PathVariable String id) {
		List<EmployeeDTO> employees = empList();
		EmployeeDTO empResult = null;
		HttpStatus httpStatus = HttpStatus.NOT_FOUND;
		for (EmployeeDTO emp : employees) {
			if (emp.getEmpId().equals(Long.valueOf(id))) {
				empResult = emp;
				httpStatus = HttpStatus.OK;
				break;
			}
		}
		return new ResponseEntity<>(empResult, httpStatus);
	}

	@PostMapping("/employees")
	public ResponseEntity<Object> createEmployee(@RequestBody EmployeeDTO employee) {
		
		EmployeeController.employees.add(employee);
		
		return new ResponseEntity<>("Employee is created successfully", HttpStatus.CREATED);
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<Object> updateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeDTO employee) {

		EmployeeDTO updatedEmp = employee;
		updatedEmp.setEmpId(id);
		// Update the element at a certain position in an ArrayList.
		List<EmployeeDTO> employees = EmployeeController.employees;
		if (employees.contains(updatedEmp)) {
			int i = employees.indexOf(updatedEmp);
			employees.set(i, updatedEmp);
		}
		return new ResponseEntity<>("Employee is updated successsfully", HttpStatus.OK);
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Object> deleteEmployee(@PathVariable("id") Long id) {

		List<EmployeeDTO> employees = EmployeeController.employees;
		// Retrieve object by Id
		EmployeeDTO employee = employees.stream().filter(a -> a.getEmpId().equals(id)).collect(Collectors.toList())
				.get(0);

		// delete from list
		if (employees.contains(employee)) {
			int i = employees.indexOf(employee);
			employees.remove(i);
		}

		return new ResponseEntity<>("Employee is deleted successsfully", HttpStatus.OK);
	}

	private static List<EmployeeDTO> empList() {
		List<EmployeeDTO> tempEmployees = new ArrayList<>();
		EmployeeDTO emp1 = new EmployeeDTO();
		emp1.setName("emp1");
		emp1.setDesignation("manager");
		emp1.setEmpId(1L);
		emp1.setSalary(3000);

		EmployeeDTO emp2 = new EmployeeDTO();
		emp2.setName("emp2");
		emp2.setDesignation("developer");
		emp2.setEmpId(2L);
		emp2.setSalary(3000);
		tempEmployees.add(emp1);
		tempEmployees.add(emp2);

		if (EmployeeController.employees != null) {
			tempEmployees = EmployeeController.employees;
		}
		return tempEmployees;
	}

}
