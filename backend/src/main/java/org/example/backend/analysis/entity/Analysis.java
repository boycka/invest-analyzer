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

    // Projet
    private String sector;

    private String region;

    @Column(length = 3000)
    private String description;

    private String stage;

    // Finance
    private Double initialBudget;

    private String financingSource;

    private Double expectedRevenue;

    private Integer expectedRoiMonths;

    // Contexte
    private String experienceLevel;

    private String competitionLevel;

    private Boolean taxAdvantages;

    private Boolean freeZoneStatus;

    // Résultat IA
    @Column(length = 4000)
    private String analysisSummary;

    private Double viabilityScore;

    private String recommendation;

    private LocalDateTime createdAt;
}