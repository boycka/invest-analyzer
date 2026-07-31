package org.example.backend.analysis.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "analysis")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Analysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sector;
    private String region;

    @Column(length = 3000)
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

    @Column(length = 4000)
    private String analysisSummary;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String detailedResultJson;

    private Double viabilityScore;
    private String recommendation;
    private String generationSource;
    private LocalDateTime createdAt;
}