package com.vaibhavsharmaggwp.json_query_engine;

import com.vaibhavsharmaggwp.json_query_engine.dto.InsertResponseDto;
import com.vaibhavsharmaggwp.json_query_engine.entity.DatasetRecord;
import com.vaibhavsharmaggwp.json_query_engine.repository.DatasetRecordRepository;
import com.vaibhavsharmaggwp.json_query_engine.service.DatasetService;
import com.vaibhavsharmaggwp.json_query_engine.strategy.QueryStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DatasetServiceTest {

    @Mock
    private DatasetRecordRepository repository;

    @Mock
    private QueryStrategy mockStrategy;

    private DatasetService datasetService;

    @BeforeEach
    void setup() {
        // Manually injecting the mocked strategy list into the service
        datasetService = new DatasetService(repository, List.of(mockStrategy));
    }

    @Test
    void insertRecord_ShouldReturnSuccessResponse() {
        // Arrange
        String datasetName = "employee_dataset";
        Map<String, Object> payload = Map.of("name", "John Doe", "age", 45);

        DatasetRecord savedRecord = new DatasetRecord();
        savedRecord.setId(1L);
        savedRecord.setDatasetName(datasetName);
        savedRecord.setRecordData(payload);

        when(repository.save(any(DatasetRecord.class))).thenReturn(savedRecord);

        // Act
        InsertResponseDto response = datasetService.insertRecord(datasetName, payload);

        // Assert
        assertNotNull(response);
        assertEquals("Record added successfully", response.getMessage());
        assertEquals(1L, response.getRecordId());
        verify(repository, times(1)).save(any(DatasetRecord.class));
    }

    @Test
    void queryDataset_ShouldThrowException_WhenDatasetEmpty() {
        // Arrange
        String datasetName = "empty_dataset";
        when(repository.findByDatasetName(datasetName)).thenReturn(Collections.emptyList());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                datasetService.queryDataset(datasetName, "department", null, "asc")
        );

        assertTrue(exception.getMessage().contains("Dataset not found or empty"));
        verify(mockStrategy, never()).execute(any(), any(), any(), any());
    }

    @Test
    void queryDataset_ShouldExecuteStrategy_WhenValidParametersProvided() {
        // Arrange
        String datasetName = "employee_dataset";
        DatasetRecord record = new DatasetRecord(1L, datasetName, Map.of("dept", "Engineering"), null);

        when(repository.findByDatasetName(datasetName)).thenReturn(List.of(record));
        when(mockStrategy.supports("dept", null)).thenReturn(true);
        when(mockStrategy.execute(any(), eq("dept"), isNull(), eq("asc")))
                .thenReturn(Map.of("groupedRecords", "success"));

        // Act
        Map<String, Object> result = datasetService.queryDataset(datasetName, "dept", null, "asc");

        // Assert
        assertTrue(result.containsKey("groupedRecords"));
        verify(mockStrategy, times(1)).execute(any(), eq("dept"), isNull(), eq("asc"));
    }
}
