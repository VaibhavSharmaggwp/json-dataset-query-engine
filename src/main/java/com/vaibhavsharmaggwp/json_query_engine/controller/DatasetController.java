package com.vaibhavsharmaggwp.json_query_engine.controller;

import com.vaibhavsharmaggwp.json_query_engine.dto.InsertResponseDto;
import com.vaibhavsharmaggwp.json_query_engine.service.DatasetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dataset")
@RequiredArgsConstructor
public class DatasetController {
    private final DatasetService datasetService;

    // POST /api/dataset/{datasetName}/record
    @PostMapping("/{datasetName}/record")
    public ResponseEntity<InsertResponseDto> insertRecord(
            @PathVariable String datasetName,
            @RequestBody Map<String, Object> payload) {
        // Robust error handling: Agar user ne khali JSON bheja, toh 400 Bad Request return karenge.
        if (payload == null || payload.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        InsertResponseDto response = datasetService.insertRecord(datasetName, payload);
        // HTTP 201 Created is the standard REST status code for successful creation
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/dataset/{datasetName}/query
    @GetMapping("/{datasetName}/query")
    public ResponseEntity<?> queryDataset(
            @PathVariable String datasetName,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "asc") String order) {

        try {
            Map<String, Object> response = datasetService.queryDataset(datasetName, groupBy, sortBy, order);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Robust error handling
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
