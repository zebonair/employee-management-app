package app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import app.utilities.Exceptions;

public record EmployeeUpdateDto(
    @NotBlank(message = Exceptions.EMPLOYEE_FULLNAME_REQUIRED) String fullName,
    @Size(max = 512) String address,
    @Size(max = 64) String phone,
    @Email String email,
    Long departmentId
) {}