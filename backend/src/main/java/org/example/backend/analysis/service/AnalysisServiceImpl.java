package org.example.backend.analysis.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.analysis.dto.request.AnalysisRequest;
import org.example.backend.analysis.dto.response.AnalysisResponse;
import org.example.backend.analysis.entity.Analysis;
import org.example.backend.analysis.repository.AnalysisRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService {

    private final AnalysisRepository repository;

    @Override
    public AnalysisResponse analyzeProject(AnalysisRequest request) {

        double score = calculateScore(request);

        String recommendation;

        if(score >=80){

            recommendation="Projet fortement recommandé";

        }else if(score>=60){

            recommendation="Projet prometteur";

        }else{

            recommendation="Projet risqué";

        }

        Analysis analysis=Analysis.builder()

                .sector(request.getSector())
                .region(request.getRegion())
                .description(request.getDescription())
                .stage(request.getStage())

                .initialBudget(request.getInitialBudget())
                .financingSource(request.getFinancingSource())
                .expectedRevenue(request.getExpectedRevenue())
                .expectedRoiMonths(request.getExpectedRoiMonths())

                .experienceLevel(request.getExperienceLevel())
                .competitionLevel(request.getCompetitionLevel())
                .taxAdvantages(request.getTaxAdvantages())
                .freeZoneStatus(request.getFreeZoneStatus())

                .analysisSummary(generateSummary(request,score))

                .viabilityScore(score)

                .recommendation(recommendation)

                .createdAt(LocalDateTime.now())

                .build();

        repository.save(analysis);

        return AnalysisResponse.builder()

                .id(analysis.getId())

                .analysisSummary(analysis.getAnalysisSummary())

                .viabilityScore(score)

                .recommendation(recommendation)

                .build();
    }

    private double calculateScore(AnalysisRequest request){

        double score = 40;

        if(request.getInitialBudget() >= 100000)
            score += 15;

        if(request.getExpectedRevenue() >= request.getInitialBudget()*2)
            score += 20;

        else if(request.getExpectedRevenue() >= request.getInitialBudget())
            score += 10;

        if(Boolean.TRUE.equals(request.getTaxAdvantages()))
            score += 10;

        if(Boolean.TRUE.equals(request.getFreeZoneStatus()))
            score += 10;

        switch (request.getExperienceLevel()){

            case "Expert" -> score += 15;

            case "Intermédiaire" -> score += 8;

            case "Débutant" -> score += 2;

        }

        switch(request.getCompetitionLevel()){

            case "Peu de concurrents" -> score += 10;

            case "Concurrence moyenne" -> score += 5;

            case "Concurrence élevée" -> score -=5;

        }

        return Math.min(score,100);

    }

    private String generateSummary(AnalysisRequest request,double score){

                return """
        
                        Le projet appartient au secteur %s.
                        
                        Il est localisé dans la région %s.
                        
                        Le budget initial déclaré est de %.2f DH.
                        
                    
                        Le chiffre d'affaires attendu est de %.2f DH.
                        
                        Le score global de viabilité est de %.0f/100.
                        
                        """
                .formatted(

                        request.getSector(),

                        request.getRegion(),

                        request.getInitialBudget(),

                        request.getExpectedRevenue(),

                        score

                );

    }

}