package app.utilities;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

public final class Exceptions {
    private Exceptions() {}

    public static final String EMPLOYEE_FULLNAME_REQUIRED = "Employee fullName is required";
    public static final String DEPARTMENT_NAME_REQUIRED = "Department name is required";
    public static final String INVALID_DEPARTMENT_ID = "Invalid departmentId";
    public static final String DEPARTMENT_NAME_EXISTS = "Department name already exists";

    public static BadRequestException badRequest(String message) {
        return new BadRequestException(message);
    }

    public static NotFoundException notFound(String resource) {
        return new NotFoundException("Resource not found " + resource);
    }

    public static WebApplicationException conflict(String message) {
        return new WebApplicationException(message, Response.Status.CONFLICT);
    }
}