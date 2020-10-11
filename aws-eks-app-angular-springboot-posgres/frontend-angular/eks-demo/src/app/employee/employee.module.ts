import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { EmployeeRoutingModule } from './employee-routing.module';
import { EmployeeComponent } from './employee.component';
import { EmployeeCreateComponent } from './employee-create/employee-create.component';
import { EmployeeListComponent } from './employee-list/employee-list.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [EmployeeComponent, EmployeeCreateComponent, EmployeeListComponent],
  imports: [
    CommonModule,
    EmployeeRoutingModule,
    ReactiveFormsModule  
  ] 
})
export class EmployeeModule { }
