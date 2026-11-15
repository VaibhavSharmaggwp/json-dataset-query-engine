package com.vaibhavsharmaggwp.json_query_engine.controller;

import com.vaibhavsharmaggwp.json_query_engine.dto.InsertResponseDto;
import com.vaibhavsharmaggwp.json_query_engine.service.DatasetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dataset")
@RequiredArgsConstructor
@Tag(name = "Dataset Query Engine", description = "Endpoints for dynamic dataset record ingestion, grouping, and sorting")
public class DatasetController {
    private final DatasetService datasetService;

    // POST /api/dataset/{datasetName}/record
    @PostMapping("/{datasetName}/record")
    @Operation(summary = "Insert Record", description = "Persist an arbitrary JSON record into a specified dataset")
    public ResponseEntity<InsertResponseDto> insertRecord(
            @Parameter(description = "Target dataset name", example = "employee_dataset")
            @PathVariable String datasetName,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "JSON document payload to persist")
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
    @Operation(summary = "Query Dataset", description = "Query dataset records using dynamic groupBy or sortBy operators")
    public ResponseEntity<?> queryDataset(
            @Parameter(description = "Target dataset name", example = "employee_dataset")
            @PathVariable String datasetName,
            @Parameter(description = "Field name to group records by", example = "department")
            @RequestParam(required = false) String groupBy,
            @Parameter(description = "Field name to sort records by", example = "age")
            @RequestParam(required = false) String sortBy,
            @Parameter(description = "Sort direction: asc or desc", example = "asc")
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
