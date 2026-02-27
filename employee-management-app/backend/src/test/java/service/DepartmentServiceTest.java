package service;

import app.dto.DepartmentCountDto;
import app.model.Department;
import app.repository.DepartmentRepository;
import app.service.DepartmentService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@QuarkusTest
class DepartmentServiceTest {

    @Inject
    DepartmentService service;

    @InjectMock
    DepartmentRepository repo;

    @Test
    void create_trims_and_persists_when_unique() {
        when(repo.findByNameIgnoreCase("hr")).thenReturn(null);

        Department created = service.create("  HR  ");

        ArgumentCaptor<Department> cap = ArgumentCaptor.forClass(Department.class);
        verify(repo).persist(cap.capture());
        assertEquals("HR", cap.getValue().getName());
        assertEquals("HR", created.getName());
    }

    @Test
    void create_conflict_when_duplicate() {
        when(repo.findByNameIgnoreCase("hr")).thenReturn(new Department("HR"));
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> service.create("hr"));
        assertEquals(Response.Status.CONFLICT.getStatusCode(), ex.getResponse().getStatus());
    }

    @Test
    void update_conflict_when_renaming_to_existing_other() {
        Department current = new Department("Sales");
        current.setId(2L);
        when(repo.find(2L)).thenReturn(current);
        Department existing = new Department("HR");
        existing.setId(1L);

        when(repo.findByNameIgnoreCase("HR")).thenReturn(existing);

        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> service.update(2L, "HR"));
        assertEquals(Response.Status.CONFLICT.getStatusCode(), ex.getResponse().getStatus());
    }

    @Test
    void delete_unassigns_then_deletes() {
        Department d = new Department("Ops");
        d.setId(5L);
        when(repo.find(5L)).thenReturn(d);

        service.delete(5L);

        verify(repo).unassignEmployees(d);
        verify(repo).delete(d);
    }

    @Test
    void listWithCounts_includes_unassigned_first() {
        reset(repo);
        when(repo.countsPerDepartment()).thenReturn(List.of(
                new DepartmentCountDto(1L, "HR", 2),
                new DepartmentCountDto(2L, "Sales", 3)
        ));
        when(repo.countUnassigned()).thenReturn(4L);

        var result = service.listWithCounts();

        assertEquals(3, result.size());
        assertEquals("not assigned", result.get(0).name());
        assertEquals(4L, result.get(0).count());
    }
}