import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import { DepartmentApiService } from '../api/department-api.service';
import { EmployeeApiService } from '../api/employee-api.service';
import { Employee, EmployeeCreateDto, EmployeeSummary } from '../api/models';

@Component({
  selector: 'app-department',
  standalone: false,
  templateUrl: './department.component.html',
  styleUrl: './department.component.scss'
})
export class DepartmentComponent implements OnInit {
  departmentId: number | null = null;
  isUnassigned = false;

  employees: Employee[] = [];
  loading = false;
  error: string | null = null;

  fullName = '';
  address = '';
  phone = '';
  email = '';

  searchQuery = '';
  searchResults: EmployeeSummary[] = [];

  departmentName: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private departmentApi: DepartmentApiService,
    private employeeApi: EmployeeApiService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(pm => {
      const idParam = pm.get('id');
      this.isUnassigned = !idParam;

      if (this.isUnassigned) {
        this.departmentId = null;
        this.departmentName = 'Unassigned';
      } else {
        this.departmentId = Number(idParam);
        this.departmentName = null;

        if (this.departmentId != null && !Number.isNaN(this.departmentId)) {
          this.departmentApi.list().subscribe({
            next: depts => {
              const match = (depts ?? []).find(d => d.id === this.departmentId);
              this.departmentName = match?.name ?? `Department ${this.departmentId}`;
            },
            error: () => {
              this.departmentName = `Department ${this.departmentId}`;
            }
          });
        }
      }

      this.refreshEmployees();
    });
  }

  refreshEmployees(): void {
    this.loading = true;
    this.error = null;

    if (this.isUnassigned) {
      this.employeeApi.search('').subscribe({
        next: rows => {
          const unassigned = (rows ?? []).filter(r => r.departmentId == null);
          this.employees = unassigned.map(r => ({
            id: r.id,
            fullName: r.fullName,
            department: null
          }));
          this.loading = false;
        },
        error: err => {
          this.error = err?.error?.message ?? 'Failed to load unassigned employees';
          this.loading = false;
        }
      });
      return;
    }

    if (this.departmentId == null || Number.isNaN(this.departmentId)) {
      this.error = 'Invalid department id';
      this.loading = false;
      return;
    }

    this.departmentApi.listEmployees(this.departmentId).subscribe({
      next: data => {
        this.employees = data ?? [];
        this.loading = false;
      },
      error: err => {
        this.error = err?.error?.message ?? 'Failed to load employees';
        this.loading = false;
      }
    });
  }

  addEmployee(): void {
    const name = (this.fullName ?? '').trim();
    if (!name) return;

    const dto: EmployeeCreateDto = {
      fullName: name,
      address: this.address || null,
      phone: this.phone || null,
      email: this.email || null,
      departmentId: this.isUnassigned ? null : this.departmentId
    };

    this.employeeApi.create(dto).subscribe({
      next: () => {
        this.fullName = '';
        this.address = '';
        this.phone = '';
        this.email = '';
        this.refreshEmployees();
      },
      error: err => {
        this.error = err?.error?.message ?? 'Failed to create employee';
      }
    });
  }

  deleteEmployee(e: Employee): void {
    this.employeeApi.delete(e.id).subscribe({
      next: () => this.refreshEmployees(),
      error: err => {
        this.error = err?.error?.message ?? 'Failed to delete employee';
      }
    });
  }

  onSearch(): void {
    const q = (this.searchQuery ?? '').trim();
    if (!q) {
      this.searchResults = [];
      return;
    }

    this.employeeApi.search(q).subscribe({
      next: res => this.searchResults = res ?? [],
      error: () => this.searchResults = []
    });
  }

  openEmployee(r: EmployeeSummary): void {
    this.router.navigate(['/employees', r.id]);
  }

  openEmployeeCard(e: Employee): void {
    this.router.navigate(['/employees', e.id]);
  }
}
