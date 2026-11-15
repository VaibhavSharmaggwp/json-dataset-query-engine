package com.vaibhavsharmaggwp.json_query_engine.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SortByStrategyTest {

    private SortByStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new SortByStrategy();
    }

    @Test
    void supports_ShouldReturnTrue_WhenSortByProvided() {
        assertTrue(strategy.supports(null, "age"));
        assertTrue(strategy.supports("dept", "age"));
    }

    @Test
    void supports_ShouldReturnFalse_WhenSortByNullOrEmpty() {
        assertFalse(strategy.supports("dept", null));
        assertFalse(strategy.supports("dept", ""));
        assertFalse(strategy.supports("dept", "   "));
    }

    @Test
    void execute_ShouldSortNumbersAscending() {
        // Arrange
        List<Map<String, Object>> records = List.of(
                Map.of("id", 1, "name", "John", "age", 30),
                Map.of("id", 2, "name", "Jane", "age", 25),
                Map.of("id", 3, "name", "Alice", "age", 28)
        );

        // Act
        Map<String, Object> result = strategy.execute(records, null, "age", "asc");

        // Assert
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> sorted = (List<Map<String, Object>>) result.get("sortedRecords");

        assertEquals(3, sorted.size());
        assertEquals(25, sorted.get(0).get("age"));
        assertEquals(28, sorted.get(1).get("age"));
        assertEquals(30, sorted.get(2).get("age"));
    }

    @Test
    void execute_ShouldSortNumbersDescending() {
        // Arrange
        List<Map<String, Object>> records = List.of(
                Map.of("id", 1, "name", "John", "age", 30),
                Map.of("id", 2, "name", "Jane", "age", 25),
                Map.of("id", 3, "name", "Alice", "age", 39)
        );

        // Act
        Map<String, Object> result = strategy.execute(records, null, "age", "desc");

        // Assert
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> sorted = (List<Map<String, Object>>) result.get("sortedRecords");

        assertEquals(39, sorted.get(0).get("age"));
        assertEquals(30, sorted.get(1).get("age"));
        assertEquals(25, sorted.get(2).get("age"));
    }

    @Test
    void execute_ShouldSortStringsAlphabetically() {
        // Arrange
        List<Map<String, Object>> records = List.of(
                Map.of("name", "Charlie"),
                Map.of("name", "Alice"),
                Map.of("name", "Bob")
        );

        // Act
        Map<String, Object> result = strategy.execute(records, null, "name", "asc");

        // Assert
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> sorted = (List<Map<String, Object>>) result.get("sortedRecords");

        assertEquals("Alice", sorted.get(0).get("name"));
        assertEquals("Bob", sorted.get(1).get("name"));
        assertEquals("Charlie", sorted.get(2).get("name"));
    }

    @Test
    void execute_ShouldHandleNullOrMissingKeysGracefully() {
        // Arrange
        Map<String, Object> r1 = new HashMap<>();
        r1.put("name", "John");
        r1.put("age", 30);

        Map<String, Object> r2 = new HashMap<>();
        r2.put("name", "Unknown"); // no age

        Map<String, Object> r3 = new HashMap<>();
        r3.put("name", "Jane");
        r3.put("age", 20);

        List<Map<String, Object>> records = List.of(r1, r2, r3);

        // Act
        Map<String, Object> result = strategy.execute(records, null, "age", "asc");

        // Assert
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> sorted = (List<Map<String, Object>>) result.get("sortedRecords");

        // Valid values come first (20, 30), null comes last
        assertEquals(20, sorted.get(0).get("age"));
        assertEquals(30, sorted.get(1).get("age"));
        assertNull(sorted.get(2).get("age"));
    }
}
