import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  //{ path: 'employees', loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule) }
  { path: '', loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule) }
 
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
