import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Department, DepartmentCountDto, DepartmentCreateDto, Employee } from './models';

@Injectable({ providedIn: 'root' })
export class DepartmentApiService {
  constructor(private http: HttpClient) {}

  list(): Observable<Department[]> {
    return this.http.get<Department[]>('/api/departments');
  }

  getCounts(): Observable<DepartmentCountDto[]> {
    return this.http.get<DepartmentCountDto[]>('/api/departments/counts');
  }

  create(dto: DepartmentCreateDto): Observable<Department> {
    return this.http.post<Department>('/api/departments', dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`/api/departments/${id}`);
  }

  listEmployees(departmentId: number): Observable<Employee[]> {
    return this.http.get<Employee[]>(`/api/departments/${departmentId}/employees`);
  }
}
