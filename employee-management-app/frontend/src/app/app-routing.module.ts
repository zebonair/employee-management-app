import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { DepartmentComponent } from './department/department.component';
import {EmployeeComponent} from './employee/employee.component';

const routes: Routes = [
  { path: '', component: DashboardComponent },
  { path: 'departments/:id', component: DepartmentComponent },
  { path: 'unassigned', component: DepartmentComponent },
  { path: 'employees/:id', component: EmployeeComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
