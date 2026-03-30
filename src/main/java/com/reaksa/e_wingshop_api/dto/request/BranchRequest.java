package com.reaksa.e_wingshop_api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BranchRequest {

    @NotBlank @Size(max = 100)
    private String name;

    @NotBlank
    private String address;

    @DecimalMin("-90.0") @DecimalMax("90.0")
    private BigDecimal latitude;

    @DecimalMin("-180.0") @DecimalMax("180.0")
    private BigDecimal longitude;

    @Pattern(regexp = "^[+]?[0-9]{8,15}$")
    private String phone;
}
