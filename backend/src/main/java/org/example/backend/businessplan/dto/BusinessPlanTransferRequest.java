package org.example.backend.businessplan.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record BusinessPlanTransferRequest(
        @NotBlank String projectName,
        @NotBlank String companyName,
        @NotBlank String sector,
        @NotBlank String analysisSummary,
        @NotNull @DecimalMin("0.0") BigDecimal investmentAmount,
        @NotNull @DecimalMin("0.0") BigDecimal targetReturn,
        @NotBlank String riskLevel
) {
}