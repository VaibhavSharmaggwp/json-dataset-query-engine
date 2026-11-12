package com.vaibhavsharmaggwp.json_query_engine.repository;

import com.vaibhavsharmaggwp.json_query_engine.entity.DatasetRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DatasetRecordRepository extends JpaRepository<DatasetRecord, Long> {
    // Spring Data JPA automatically is method ka SQL (SELECT * FROM dataset_records WHERE dataset_name = ?) generate karega.
    List<DatasetRecord> findByDatasetName(String datasetName);
}
