package org.example.backend.history.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.history.dto.HistoryResponse;
import org.example.backend.history.service.HistoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
public class HistoryController {

    private final HistoryService historyService;

    @GetMapping("/history")
    public List<HistoryResponse> getHistory() {

        return historyService.getHistory();

    }

}