package org.example.backend.analysis.dto.request;

import lombok.Data;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class AnalysisRequest {

    @NotBlank
    private String sector;

    @NotBlank
    private String region;

    @NotBlank
    private String description;

    @NotBlank
    private String stage;

    @NotNull
    @DecimalMin("0.0")
    private Double initialBudget;

    @NotBlank
    private String financingSource;

    @NotNull
    @DecimalMin("0.0")
    private Double expectedRevenue;

    @NotNull
    @Min(1)
    private Integer expectedRoiMonths;

    @NotBlank
    private String experienceLevel;

    @NotBlank
    private String competitionLevel;

    @NotNull
    private Boolean taxAdvantages;

    @NotNull
    private Boolean freeZoneStatus;

}
