import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Employee, EmployeeCreateDto, EmployeeSummary } from './models';

@Injectable({ providedIn: 'root' })
export class EmployeeApiService {
  constructor(private http: HttpClient) {}

  search(query: string): Observable<EmployeeSummary[]> {
    const params = new HttpParams().set('query', query ?? '');
    return this.http.get<EmployeeSummary[]>('/api/employees/search', { params });
  }

  create(dto: EmployeeCreateDto): Observable<Employee> {
    return this.http.post<Employee>('/api/employees', dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`/api/employees/${id}`);
  }

  get(id: number): Observable<Employee> {
    return this.http.get<Employee>(`/api/employees/${id}`);
  }
}
