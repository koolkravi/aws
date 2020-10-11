import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Employee } from '../model/employee';
import { Api } from '../../constant/api.constant';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';

const httpOptions = {
  headers: new HttpHeaders({
    'Content-Type': 'application/json',
    Authorization: 'my-auth-token'
  })
};

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private employeeApi = Api.employees;
  constructor(private httpClient: HttpClient) { }

  getEmployees(): Observable<Employee[]> {
    console.log(this.employeeApi);
    return this.httpClient.get<Employee[]>(this.employeeApi);
  }

  addEmployee(employee: Employee): Observable<string> {
    return this.httpClient.post<string>(this.employeeApi, employee, httpOptions);
  }

  deleteEmployee(empId: number): Observable<{}> {
    const url = `${this.employeeApi}/${empId}`;
    return this.httpClient.delete(url, httpOptions);
  }

  updateEmployee(employee: Employee): Observable<Employee> {
    const url = `${this.employeeApi}/${employee.empId}`;
    return this.httpClient.put<Employee>(url, employee, httpOptions);
  }

  getEmployeeDetail(empId: number): Observable<Employee> {
    const url = `${this.employeeApi}/${empId}`;
    return this.httpClient.get<Employee>(url, httpOptions);
  }

  searchEmployee(term: string): Observable<Employee[]> {
    term = term.trim();
    // Add safe, URL encoded search parameter if there is a search term
    const options = term ?
      { params: new HttpParams().set('name', term) } : {};

    return this.httpClient.get<Employee[]>(this.employeeApi, options);
  }

}
