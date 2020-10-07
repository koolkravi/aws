import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Employee } from '../model/employee';
import { Api } from '../../constant/api.constant';


@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private employeeApi = Api.employees;
  constructor(private httpClient: HttpClient) { }

  getEmployees() {
    console.log(this.employeeApi);
    return this.httpClient.get<Employee>(this.employeeApi);
  }
}
