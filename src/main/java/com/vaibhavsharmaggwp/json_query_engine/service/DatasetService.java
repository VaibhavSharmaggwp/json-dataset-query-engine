package com.vaibhavsharmaggwp.json_query_engine.service;

import com.vaibhavsharmaggwp.json_query_engine.dto.InsertResponseDto;
import com.vaibhavsharmaggwp.json_query_engine.entity.DatasetRecord;
import com.vaibhavsharmaggwp.json_query_engine.repository.DatasetRecordRepository;
import com.vaibhavsharmaggwp.json_query_engine.strategy.QueryStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DatasetService {

    private final DatasetRecordRepository repository;

    // Spring automatically sabhi QueryStrategy implementations ko is list mein daal dega
    private final List<QueryStrategy> queryStrategies;

    public InsertResponseDto insertRecord(String datasetName, Map<String, Object> payload) {
        DatasetRecord record = DatasetRecord.builder()
                .datasetName(datasetName)
                .recordData(payload)
                .build();
        DatasetRecord savedRecord = repository.save(record);
        return InsertResponseDto.builder()
                .message("Record added successfully")
                .dataset(datasetName)
                .recordId(savedRecord.getId())
                .build();
    }

    public Map<String, Object> queryDataset(String datasetName, String groupBy, String sortBy, String order) {
        // 1. Database se dataset nikalna
        List<DatasetRecord> records = repository.findByDatasetName(datasetName);

        if (records.isEmpty()) {
            throw new IllegalArgumentException("Dataset not found or empty: " + datasetName);
        }

        // 2. Entity list ko raw Map list mein convert karna (taaki humari strategy pure data pe kaam kare)
        List<Map<String, Object>> rawRecords = records.stream()
                .map(DatasetRecord::getRecordData)
                .collect(Collectors.toList());

        // 3. Strategy Pattern in action: Jo strategy support karti hai, usko dhoondho aur execute karo
        for (QueryStrategy strategy : queryStrategies) {
            if (strategy.supports(groupBy, sortBy)) {
                return strategy.execute(rawRecords, groupBy, sortBy, order);
            }
        }

        throw new IllegalArgumentException("Invalid query parameters provided. Must specify groupBy or sortBy.");
    }
}
