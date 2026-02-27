import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DepartmentApiService } from '../api/department-api.service';
import { DepartmentCountDto } from '../api/models';

@Component({
  selector: 'dashboard',
  standalone: false,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  counts: DepartmentCountDto[] = [];
  newDepartmentName = '';
  loading = false;
  error: string | null = null;

  constructor(
    private departmentApi: DepartmentApiService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.refreshCounts();
  }

  refreshCounts(): void {
    this.loading = true;
    this.error = null;

    this.departmentApi.getCounts().subscribe({
      next: data => {
        this.counts = data ?? [];
        this.loading = false;
      },
      error: err => {
        this.error = err?.error?.message ?? 'Failed to load department counts';
        this.loading = false;
      }
    });
  }

  openDepartment(d: DepartmentCountDto): void {
    if (d.id == null) {
      this.router.navigate(['/unassigned']);
    } else {
      this.router.navigate(['/departments', d.id]);
    }
  }

  addDepartment(): void {
    const name = (this.newDepartmentName ?? '').trim();
    if (!name) return;

    this.departmentApi.create({ name }).subscribe({
      next: () => {
        this.newDepartmentName = '';
        this.refreshCounts();
      },
      error: err => {
        this.error = err?.error?.message ?? 'Failed to create department';
      }
    });
  }

  deleteDepartment(d: DepartmentCountDto, evt: MouseEvent): void {
    evt.preventDefault();
    evt.stopPropagation();

    if (d.id == null) return;

    this.departmentApi.delete(d.id).subscribe({
      next: () => this.refreshCounts(),
      error: err => {
        this.error = err?.error?.message ?? 'Failed to delete department';
      }
    });
  }
}
