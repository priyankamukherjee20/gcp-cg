package com.test.gcp.payload;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

@Data @EqualsAndHashCode(callSuper = false) @JsonInclude(Include.NON_NULL) @Generated
public class AdminDTO {

    private String employeeId;

    @NotBlank(message = "{name.null.message}")
    private String name;

    @NotBlank(message = "{password.null.message}")
    private String password;

    @NotBlank(message = "{email.null.message}") @Email(message = "{email.format.message}")
    private String email;

    @Size(min = 10, message = "{address.null.message}", max = 50)
    private String address;

    @Valid
    private Set<Role> roles;
}
