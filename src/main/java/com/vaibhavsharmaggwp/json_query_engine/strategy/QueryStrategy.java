package com.vaibhavsharmaggwp.json_query_engine.strategy;

import java.util.List;
import java.util.Map;

public interface QueryStrategy {
    // Ye check karega ki kaunsi strategy active honi chahiye based on request parameters
    boolean supports(String groupBy, String sortBy);

    // Ye actual algorithmic processing handle karega
    Map<String, Object> execute(List<Map<String, Object>> rawRecords, String groupBy, String sortBy, String order);
}
