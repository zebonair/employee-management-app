package app.dto;

import app.utilities.Exceptions;
import jakarta.validation.constraints.NotBlank;

public record DepartmentCreateDto(
        @NotBlank(message = Exceptions.DEPARTMENT_NAME_REQUIRED) String name
) {}
