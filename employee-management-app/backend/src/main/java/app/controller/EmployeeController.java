package app.controller;

import app.dto.EmployeeCreateDto;
import app.dto.EmployeeUpdateDto;
import app.model.Employee;
import app.service.EmployeeService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/employees")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmployeeController {

    @Inject
    EmployeeService employeeService;

    @GET
    @Path("/{id}")
    public Employee get(@PathParam("id") Long id) {
        return employeeService.get(id);
    }

    @POST
    public Employee create(@Valid EmployeeCreateDto dto) {
        return employeeService.create(dto.fullName(), dto.address(), dto.phone(), dto.email(), dto.departmentId());
    }

    @PUT
    @Path("/{id}")
    public Employee update(@PathParam("id") Long id, @Valid EmployeeUpdateDto dto) {
        return employeeService.update(id, dto.fullName(), dto.address(), dto.phone(), dto.email(), dto.departmentId());
    }

    @DELETE
    @Path("/{id}")
    public void delete(@PathParam("id") Long id) {
        employeeService.delete(id);
    }

    @GET
    @Path("/search")
    public List<EmployeeService.Summary> search(@QueryParam("query") String query) {
        return employeeService.search(query);
    }
}
