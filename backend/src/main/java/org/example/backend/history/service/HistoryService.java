package org.example.backend.history.service;

import org.example.backend.history.dto.HistoryResponse;

import java.util.List;

public interface HistoryService {

    List<HistoryResponse> getHistory();

}