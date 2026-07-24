package org.example.backend.common;

import java.util.LinkedHashMap;
import java.util.Map;
import org.example.backend.common.util.Constants;
import org.example.backend.common.util.DateUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_PREFIX)
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("service", "backend");
        response.put("status", "UP");
        response.put("timestamp", DateUtils.now());
        return ResponseEntity.ok(response);
    }
}