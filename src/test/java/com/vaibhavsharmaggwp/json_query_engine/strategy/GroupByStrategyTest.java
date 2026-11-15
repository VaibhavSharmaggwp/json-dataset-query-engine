package com.vaibhavsharmaggwp.json_query_engine.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GroupByStrategyTest {

    private GroupByStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new GroupByStrategy();
    }

    @Test
    void supports_ShouldReturnTrue_WhenGroupByProvided() {
        assertTrue(strategy.supports("department", null));
        assertTrue(strategy.supports("role", "name"));
    }

    @Test
    void supports_ShouldReturnFalse_WhenGroupByNullOrEmpty() {
        assertFalse(strategy.supports(null, "age"));
        assertFalse(strategy.supports("", "age"));
        assertFalse(strategy.supports("   ", "age"));
    }

    @Test
    void execute_ShouldGroupRecordsCorrectly() {
        // Arrange
        List<Map<String, Object>> records = List.of(
                Map.of("id", 1, "name", "John", "department", "Engineering"),
                Map.of("id", 2, "name", "Jane", "department", "Engineering"),
                Map.of("id", 3, "name", "Alice", "department", "Marketing")
        );

        // Act
        Map<String, Object> result = strategy.execute(records, "department", null, "asc");

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey("groupedRecords"));

        @SuppressWarnings("unchecked")
        Map<String, List<Map<String, Object>>> grouped =
                (Map<String, List<Map<String, Object>>>) result.get("groupedRecords");

        assertEquals(2, grouped.size());
        assertEquals(2, grouped.get("Engineering").size());
        assertEquals(1, grouped.get("Marketing").size());
    }

    @Test
    void execute_ShouldHandleMissingKeyGracefully() {
        // Arrange
        List<Map<String, Object>> records = List.of(
                Map.of("id", 1, "name", "John", "department", "Engineering"),
                Map.of("id", 2, "name", "Ghost") // missing department
        );

        // Act
        Map<String, Object> result = strategy.execute(records, "department", null, "asc");

        // Assert
        @SuppressWarnings("unchecked")
        Map<String, List<Map<String, Object>>> grouped =
                (Map<String, List<Map<String, Object>>>) result.get("groupedRecords");

        assertTrue(grouped.containsKey("Unknown"));
        assertEquals(1, grouped.get("Unknown").size());
    }
}
