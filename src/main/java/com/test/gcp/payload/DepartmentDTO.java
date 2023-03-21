package com.test.gcp.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

@Data @EqualsAndHashCode(callSuper = false) @JsonInclude(Include.NON_NULL) @Generated
public class DepartmentDTO {

    @NotBlank(message = "{departmentId.null.message}")
    private String departmentId;

    @NotBlank(message = "{departmentname.null.message}")
    private String departmentName;
}
