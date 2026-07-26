package org.example.backend.analysis.dto.request;

import lombok.Data;

@Data
public class AnalysisRequest {

    private String sector;

    private String region;

    private String description;

    private String stage;

    private Double initialBudget;

    private String financingSource;

    private Double expectedRevenue;

    private Integer expectedRoiMonths;

    private String experienceLevel;

    private String competitionLevel;

    private Boolean taxAdvantages;

    private Boolean freeZoneStatus;

}