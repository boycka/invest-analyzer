package org.example.backend.history.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.analysis.entity.Analysis;
import org.example.backend.analysis.repository.AnalysisRepository;
import org.example.backend.history.dto.HistoryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {


    private final AnalysisRepository repository;



    @Override
    public List<HistoryResponse> getHistory() {

        return repository.findAllByOrderByCreatedAtDesc()

                .stream()

                .map(analysis -> new HistoryResponse(

                        analysis.getId(),

                        analysis.getSector(),

                        analysis.getViabilityScore(),

                        analysis.getRecommendation(),

                        analysis.getCreatedAt()

                ))

                .toList();

    }



    // NOUVELLE METHODE
    @Override
    public HistoryResponse getById(Long id) {


        Analysis analysis = repository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException("Analyse introuvable avec id : " + id)
                );


        return new HistoryResponse(

                analysis.getId(),

                analysis.getSector(),

                analysis.getViabilityScore(),

                analysis.getRecommendation(),

                analysis.getCreatedAt()

        );

    }

}