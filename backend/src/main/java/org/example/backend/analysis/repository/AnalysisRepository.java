package org.example.backend.analysis.repository;

import org.example.backend.analysis.entity.Analysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, Long> {

    List<Analysis> findAllByOrderByCreatedAtDesc();

}