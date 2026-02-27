package app.service;

import app.dto.DepartmentCountDto;
import app.model.Department;
import app.model.Employee;
import app.repository.DepartmentRepository;
import app.utilities.Exceptions;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DepartmentService {

    @Inject
    DepartmentRepository departmentRepository;

    public static class DepartmentCount {
        public Long id;
        public String name;
    }

    public List<Department> listAll() {
        return departmentRepository.listAllOrdered();
    }

    @Transactional
    public Department create(String name) {
        String trimmed = name.trim();
        validateUniqueName(trimmed, null);
        Department d = new Department(trimmed);
        departmentRepository.persist(d);
        return d;
    }

    @Transactional
    public Department update(Long id, String name) {
        Department d = requireDepartment(id);

        String trimmed = name.trim();
        validateUniqueName(trimmed, id);

        d.setName(trimmed);
        return d;
    }

    @Transactional
    public void delete(Long id) {
        Department d = requireDepartment(id);
        departmentRepository.unassignEmployees(d);
        departmentRepository.delete(d);
    }

    public List<Employee> listEmployees(Long id) {
        Department d = requireDepartment(id);
        return departmentRepository.listEmployees(d);
    }

    public List<DepartmentCountDto> listWithCounts() {
        List<DepartmentCountDto> result = new ArrayList<>(departmentRepository.countsPerDepartment());
        long notAssigned = departmentRepository.countUnassigned();
        result.add(0, new DepartmentCountDto(null, "not assigned", notAssigned));
        return result;
    }

    private Department requireDepartment(Long id) {
        Department d = departmentRepository.find(id);
        if (d == null) throw Exceptions.notFound("Department " + id);
        return d;
    }

    private void validateUniqueName(String name, Long currentId) {
        String trimmed = name.trim();
        var existing = departmentRepository.findByNameIgnoreCase(trimmed);
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw Exceptions.conflict(Exceptions.DEPARTMENT_NAME_EXISTS);
        }
    }
}