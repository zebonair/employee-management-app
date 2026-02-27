package app.controller;

import app.dto.DepartmentCountDto;
import app.dto.DepartmentCreateDto;
import app.dto.DepartmentUpdateDto;
import app.model.Department;
import app.model.Employee;
import app.service.DepartmentService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/departments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DepartmentController {

    @Inject
    DepartmentService departmentService;

    @GET
    public List<Department> list() {
        return departmentService.listAll();
    }

    @POST
    public Department create(@Valid DepartmentCreateDto dto) {
        return departmentService.create(dto.name());
    }

    @PUT
    @Path("/{id}")
    public Department update(@PathParam("id") Long id, @Valid DepartmentUpdateDto dto) {
        return departmentService.update(id, dto.name());
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        departmentService.delete(id);
    }

    @GET
    @Path("/{id}/employees")
    public List<Employee> listEmployees(@PathParam("id") Long id) {
        return departmentService.listEmployees(id);
    }

    @GET
    @Path("/counts")
    public List<DepartmentCountDto> counts() {
        return departmentService.listWithCounts();
    }
}
