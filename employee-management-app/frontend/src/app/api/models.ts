export interface Department {
  id: number;
  name: string;
}

export interface DepartmentCountDto {
  id: number | null;
  name: string;
  count: number;
}

export interface Employee {
  id: number;
  fullName: string;
  address?: string | null;
  phone?: string | null;
  email?: string | null;
  department?: Department | null;
}

export interface EmployeeCreateDto {
  fullName: string;
  address?: string | null;
  phone?: string | null;
  email?: string | null;
  departmentId?: number | null;
}

export interface EmployeeSummary {
  id: number;
  fullName: string;
  departmentId: number | null;
  departmentName: string | null;
}

export interface DepartmentCreateDto {
  name: string;
}
