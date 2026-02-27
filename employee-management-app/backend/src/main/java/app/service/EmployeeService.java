package app.service;

import app.model.Department;
import app.model.Employee;
import app.repository.DepartmentRepository;
import app.repository.EmployeeRepository;
import app.utilities.Exceptions;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class EmployeeService {

    @Inject
    EmployeeRepository employeeRepository;

    @Inject
    DepartmentRepository departmentRepository;

    public static class Summary { public Long id; public String fullName; public Long departmentId; public String departmentName; }

    public Employee get(Long id) {
        return requireEmployee(id);
    }

    @Transactional
    public Employee create(
        String fullName,
        String address,
        String phone,
        String email,
        Long departmentId
    ) {
        Department d = departmentId != null ? resolveDepartmentOrBadRequest(departmentId) : null;
        Employee e = new Employee(fullName.trim(), address, phone, email, d);
        employeeRepository.persist(e);
        return e;
    }

    @Transactional
    public Employee update(
        Long id,
        String fullName,
        String address,
        String phone,
        String email,
        Long departmentId
    ) {
        Employee e = requireEmployee(id);
        Department d = departmentId != null ? resolveDepartmentOrBadRequest(departmentId) : null;

        e.setFullName(fullName.trim());
        e.setAddress(address);
        e.setPhone(phone);
        e.setEmail(email);
        e.setDepartment(d);
        return e;
    }

    @Transactional
    public void delete(Long id) {
        Employee e = requireEmployee(id);
        employeeRepository.delete(e);
    }

    public List<Summary> search(String query) {
        String q = (query == null ? "" : query).toLowerCase();
        List<Object[]> rows = employeeRepository.searchByNameLike(q);
        return rows.stream().map(r -> {
            Summary s = new Summary();
            s.id = (Long) r[0];
            s.fullName = (String) r[1];
            s.departmentId = (Long) r[2];
            s.departmentName = (String) r[3];
            return s;
        }).toList();
    }

    private Employee requireEmployee(Long id) {
        Employee e = employeeRepository.find(id);
        if (e == null) throw Exceptions.notFound("Employee " + id);
        return e;
    }

    private Department resolveDepartmentOrBadRequest(Long departmentId) {
        Department d = departmentRepository.find(departmentId);
        if (d == null) throw Exceptions.badRequest(Exceptions.INVALID_DEPARTMENT_ID);
        return d;
    }
}