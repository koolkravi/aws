import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FormBuilder } from '@angular/forms'
import { EmployeeService } from '../service/employee.service';

@Component({
  selector: 'app-employee-create',
  templateUrl: './employee-create.component.html',
  styleUrls: ['./employee-create.component.css']
})
export class EmployeeCreateComponent implements OnInit {

  employeeForm = this.fb.group(
    {
      name: ['emp1', Validators.required],
      designation: ['developer', Validators.required],
      salary: ['4000', Validators.required]
    }
  );

  constructor(private fb: FormBuilder, private employeeService : EmployeeService) { }

  ngOnInit(): void {
  }

  onEmployeeAddSubmit() {
    this.employeeService.addEmployee(this.employeeForm.value).subscribe(
      (response) => this.handleSuccessfulResponse(response)
    )
  }

  handleSuccessfulResponse(response) {
    console.log(response);
  }

}
