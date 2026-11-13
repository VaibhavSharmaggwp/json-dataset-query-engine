package com.vaibhavsharmaggwp.json_query_engine.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "dataset_records")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatasetRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dataset_name", nullable = false)
    private String datasetName;

    // Interview Defense: Map<String, Object> isliye use kiya kyunki JSON structure dynamic hai.
    // Agar hum strict POJO banate, toh alag datasets (jaise "Products" vs "Employees") pe code break ho jata.
    // @JdbcTypeCode(SqlTypes.JSON) Hibernate 6 ka native feature hai.
    // Ye automatically Java Map ko DB ke JSONB format mein serialize/deserialize kar dega.
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "record_data", columnDefinition = "jsonb", nullable = false)
    private Map<String, Object> recordData;

    // Database default value use karega, humein Java se set karne ki zaroorat nahi
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
