import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject, debounceTime, distinctUntilChanged, switchMap, of } from 'rxjs';
import { EmployeeApiService } from './api/employee-api.service';
import { EmployeeSummary } from './api/models';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.scss'
})
export class AppComponent {
  searchQuery = '';
  searchResults: EmployeeSummary[] = [];

  private search$ = new Subject<string>();

  constructor(
    private employeeApi: EmployeeApiService,
    private router: Router
  ) {
    this.search$
      .pipe(
        debounceTime(200),
        distinctUntilChanged(),
        switchMap(q => {
          const trimmed = (q ?? '').trim();
          if (!trimmed) return of<EmployeeSummary[]>([]);
          return this.employeeApi.search(trimmed);
        })
      )
      .subscribe(results => {
        this.searchResults = results ?? [];
      });
  }

  onSearchChange(): void {
    this.search$.next(this.searchQuery);
  }

  goToSearchResult(r: EmployeeSummary): void {
    this.searchResults = [];
    this.searchQuery = '';
    this.router.navigate(['/employees', r.id]);
  }
}
