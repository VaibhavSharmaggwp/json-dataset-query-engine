package com.vaibhavsharmaggwp.json_query_engine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsertResponseDto {
    private String message;
    private String dataset;
    private Long recordId;
}
