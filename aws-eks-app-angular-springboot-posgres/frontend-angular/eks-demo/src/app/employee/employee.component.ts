import { Component, OnInit } from '@angular/core';
import { EmployeeService } from './service/employee.service'

@Component({
  selector: 'app-employee',
  templateUrl: './employee.component.html',
  styleUrls: ['./employee.component.css']
})
export class EmployeeComponent implements OnInit {
  employees: string[];
  constructor(private employeeService: EmployeeService) { }

  ngOnInit(): void {
    this.employeeService.getEmployees().subscribe(      
      Response => this.handleSuccessfulResponse(Response)
    )
  }

  handleSuccessfulResponse(response) {
    this.employees = response;
  }

}
