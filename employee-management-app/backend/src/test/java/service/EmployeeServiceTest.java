package service;

import app.model.Department;
import app.model.Employee;
import app.repository.DepartmentRepository;
import app.repository.EmployeeRepository;
import app.service.EmployeeService;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class EmployeeServiceTest {

    @Inject
    EmployeeService service;

    @InjectMock
    EmployeeRepository employeeRepo;

    @InjectMock
    DepartmentRepository departmentRepo;

    @Test
    void get_throws_not_found_when_missing() {
        when(employeeRepo.find(99L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> service.get(99L));
    }

    @Test
    void create_bad_request_on_invalid_department() {
        when(departmentRepo.find(123L)).thenReturn(null);
        assertThrows(BadRequestException.class, () ->
                service.create("Test user 1", null, null, null, 123L));
        verify(employeeRepo, never()).persist(any());
    }

    @Test
    void update_sets_unassigned_when_department_null() {
        Department sales = new Department("Sales");
        sales.setId(5L);
        Employee e = new Employee("Test user 1", "a", "p", "e@mail", sales);
        e.setId(10L);
        when(employeeRepo.find(10L)).thenReturn(e);

        Employee updated = service.update(10L, "John Doe", "b", "q", "x@mail", null);

        assertNull(updated.getDepartment());
        assertEquals("John Doe", updated.getFullName());
        assertEquals("b", updated.getAddress());
        assertEquals("q", updated.getPhone());
        assertEquals("x@mail", updated.getEmail());
    }

    @Test
    void search_maps_rows_to_summaries() {
        when(employeeRepo.searchByNameLike("al")).thenReturn(List.of(
                new Object[]{1L, "Alice", 2L, "HR"},
                new Object[]{3L, "Alan", null, null}
        ));

        var summaries = service.search("Al");

        assertEquals(2, summaries.size());
        assertEquals(1L, summaries.get(0).id);
        assertEquals("Alice", summaries.get(0).fullName);
        assertEquals(2L, summaries.get(0).departmentId);
        assertEquals("HR", summaries.get(0).departmentName);

        assertEquals(3L, summaries.get(1).id);
        assertEquals("Alan", summaries.get(1).fullName);
        assertNull(summaries.get(1).departmentId);
        assertNull(summaries.get(1).departmentName);
    }

    @Test
    void delete_throws_not_found_when_missing() {
        when(employeeRepo.find(7L)).thenReturn(null);
        assertThrows(NotFoundException.class, () -> service.delete(7L));
        verify(employeeRepo, never()).delete(any());
    }
}