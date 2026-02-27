import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { ActivatedRoute, convertToParamMap, Router } from '@angular/router';
import { of } from 'rxjs';

import { DepartmentComponent } from './department.component';

describe('DepartmentComponent', () => {
  let component: DepartmentComponent;
  let fixture: ComponentFixture<DepartmentComponent>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DepartmentComponent],
      imports: [FormsModule],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(convertToParamMap({ id: '5' }))
          }
        },
        {
          provide: Router,
          useValue: { navigate: jasmine.createSpy('navigate') }
        }
      ]
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);

    fixture = TestBed.createComponent(DepartmentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('loads department name + employees for a department id', () => {
    const deptsReq = httpMock.expectOne('/api/departments');
    expect(deptsReq.request.method).toBe('GET');
    deptsReq.flush([
      { id: 5, name: 'Sales' },
      { id: 6, name: 'HR' }
    ]);

    const empsReq = httpMock.expectOne('/api/departments/5/employees');
    expect(empsReq.request.method).toBe('GET');
    empsReq.flush([
      { id: 10, fullName: 'John Doe', email: 'john@x.com', department: { id: 5, name: 'Sales' } }
    ]);

    expect(component.departmentId).toBe(5);
    expect(component.departmentName).toBe('Sales');
    expect(component.employees.length).toBe(1);
    expect(component.employees[0].fullName).toBe('John Doe');
  });
});
