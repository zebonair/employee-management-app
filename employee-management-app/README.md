# Employee Management App

## Backend

Backend runs on:

**http://localhost:8081**

---

## Frontend (Angular)

From the `frontend` folder:

```bash
cd frontend
npm ci
npm start
```

Frontend runs on:

**http://localhost:4200**

---

## API Proxy (No CORS Issues)

The frontend is configured to proxy `/api` to the backend.

### Configuration Files

- `frontend/proxy.conf.json`
- `frontend/package.json`

`package.json` script:

```bash
ng serve --proxy-config proxy.conf.json
```

### Example

Frontend call:

```
/api/departments/counts
```

Is forwarded to:

```
http://localhost:8081/api/departments/counts
```

---

# Features Implemented

## Dashboard

- List departments with employee counts  
  - `GET /api/departments/counts`
- Shows **“Unassigned”**  
  - Represents employees with `department = null`
- Add department  
  - `POST /api/departments`
- Delete department  
  - `DELETE /api/departments/{id}`  
  - Backend behavior: deleting a department unassigns employees first
- Global search bar  
  - `GET /api/employees/search?query=...`  
  - Clicking a result opens the employee detail page

---

## Department View

### Routes

- Assigned department: `/departments/:id`
- Unassigned: `/unassigned`

### Features

- List employees in selected department  
  - `GET /api/departments/{id}/employees`
- Add employee  
  - `POST /api/employees`
- Delete employee  
  - `DELETE /api/employees/{id}`
- Search employees  
  - `GET /api/employees/search?query=...`  
  - Results are clickable and navigate to employee detail
- Department header displays the department name  
  - Resolved by loading departments list and matching by `id`

---

## Employee Detail Page

### Route

`/employees/:id`

### Features

- Shows full employee info:
  - name
  - address
  - phone
  - email
  - department
- `GET /api/employees/{id}`
- “Go” button navigates back to department or unassigned

---

# Tests

## Backend Tests

Quarkus tests exist for services:

- Employee
- Department

Using:

- `@QuarkusTest`
- `@InjectMock`

Run backend tests:

```bash
cd backend
mvn test
```

---

## Frontend Tests

Angular component tests added for:

- `DashboardComponent`
- `DepartmentComponent`
- `EmployeeComponent`
- `AppComponent`

HTTP testing uses Angular 19 provider-based testing (no deprecated `HttpClientTestingModule`):

- `provideHttpClient()`
- `provideHttpClientTesting()`
- `HttpTestingController`

Run frontend tests:

```bash
cd frontend
npm test
```

---

# Main Frontend Code Locations

## API Models & Services

- `frontend/src/app/api/models.ts`
- `frontend/src/app/api/department-api.service.ts`
- `frontend/src/app/api/employee-api.service.ts`

## Components

- `frontend/src/app/dashboard/*`
- `frontend/src/app/department/*`
- `frontend/src/app/employee/*`

## Routing

- `frontend/src/app/app-routing.module.ts`

---

# Notes / Known Constraints

- Editing existing departments/employees via frontend is not implemented.
- Unassigned listing is implemented via employee search and filtering `departmentId == null`.
- There is no dedicated backend endpoint for listing unassigned employees.