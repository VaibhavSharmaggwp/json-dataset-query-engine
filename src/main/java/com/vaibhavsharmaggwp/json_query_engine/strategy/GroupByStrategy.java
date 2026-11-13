package com.vaibhavsharmaggwp.json_query_engine.strategy;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GroupByStrategy implements QueryStrategy {

    @Override
    public boolean supports(String groupBy, String sortBy) {
        // Agar groupBy parameter pass hua hai, toh ye strategy activate hogi
        return groupBy != null && !groupBy.trim().isEmpty();
    }

    @Override
    public Map<String, Object> execute(List<Map<String, Object>> rawRecords, String groupBy, String sortBy, String order) {

        // Java Streams ka use karke O(N) time complexity mein grouping kar rahe hain.
        // Interview Defense: We use Collectors.groupingBy with a lambda to dynamically fetch the requested field.
        Map<String, List<Map<String, Object>>> groupedData = rawRecords.stream()
                .collect(Collectors.groupingBy(
                        // Agar json mein wo field missing hai, toh "Unknown" bucket mein daal denge (Robustness!)
                        record -> String.valueOf(record.getOrDefault(groupBy, "Unknown"))
                ));

        // Exact response structure match kar rahe hain PDF assignment ke hisaab se
        return Map.of("groupedRecords", groupedData);
    }
}
