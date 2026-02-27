import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EmployeeApiService } from '../api/employee-api.service';
import { Employee } from '../api/models';

@Component({
  selector: 'app-employee',
  standalone: false,
  templateUrl: './employee.component.html',
  styleUrl: './employee.component.scss'
})
export class EmployeeComponent implements OnInit {
  employee: Employee | null = null;
  loading = false;
  error: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private employeeApi: EmployeeApiService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(pm => {
      const idParam = pm.get('id');
      const id = Number(idParam);

      if (!idParam || Number.isNaN(id)) {
        this.error = 'Invalid employee id';
        return;
      }

      this.loadEmployee(id);
    });
  }

  loadEmployee(id: number): void {
    this.loading = true;
    this.error = null;

    this.employeeApi.get(id).subscribe({
      next: e => {
        this.employee = e ?? null;
        this.loading = false;
      },
      error: err => {
        this.error = err?.error?.message ?? 'Failed to load employee';
        this.loading = false;
      }
    });
  }

  goToDepartment(): void {
    if (!this.employee) return;

    const deptId = this.employee.department?.id ?? null;
    if (deptId == null) this.router.navigate(['/unassigned']);
    else this.router.navigate(['/departments', deptId]);
  }
}
