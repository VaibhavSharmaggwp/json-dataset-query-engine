package com.vaibhavsharmaggwp.json_query_engine.strategy;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SortByStrategy implements QueryStrategy {

    @Override
    public boolean supports(String groupBy, String sortBy) {
        // Activates only if sortBy parameter is provided
        return sortBy != null && !sortBy.trim().isEmpty();
    }

    @Override
    public Map<String, Object> execute(List<Map<String, Object>> rawRecords, String groupBy, String sortBy, String order) {

        // Determine sort direction (default to ascending if null or not "desc")
        boolean isAsc = (order == null || !order.equalsIgnoreCase("desc"));

        // Custom Comparator for dynamic JSON fields
        Comparator<Map<String, Object>> dynamicComparator = (map1, map2) -> {
            Object val1 = map1.get(sortBy);
            Object val2 = map2.get(sortBy);

            // Robustness in error handling: Handle missing keys or null values (nulls last)
            if (val1 == null && val2 == null) return 0;
            if (val1 == null) return 1;
            if (val2 == null) return -1;

            // Handle numbers (Integer, Long, Double) cleanly to avoid ClassCastException
            if (val1 instanceof Number n1 && val2 instanceof Number n2) {
                int result = Double.compare(n1.doubleValue(), n2.doubleValue());
                return isAsc ? result : -result;
            }

            // If both objects are of the exact same Comparable type (e.g., String, Boolean)
            if (val1.getClass().equals(val2.getClass()) && val1 instanceof Comparable) {
                @SuppressWarnings("unchecked")
                int result = ((Comparable<Object>) val1).compareTo(val2);
                return isAsc ? result : -result;
            }

            // Fallback: convert to string and compare lexicographically
            int strResult = val1.toString().compareTo(val2.toString());
            return isAsc ? strResult : -strResult;
        };

        // O(N log N) sorting using Java Streams
        List<Map<String, Object>> sortedData = rawRecords.stream()
                .sorted(dynamicComparator)
                .collect(Collectors.toList());

        // Matches the expected JSON response exactly from the assignment specification
        return Map.of("sortedRecords", sortedData);
    }
}
