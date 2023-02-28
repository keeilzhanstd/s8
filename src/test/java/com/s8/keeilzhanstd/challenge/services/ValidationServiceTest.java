package com.s8.keeilzhanstd.challenge.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationServiceTest {

    ValidationService service = new ValidationService();

    @Test
    void catchInvalidMonth() {
            int invalid = 25;
            Exception e = assertThrows(IllegalArgumentException.class, () -> {service.validateMonth(invalid);});
            String expected = "Error: month expected within [1, 12]";
            assertEquals(expected, e.getMessage());
    }

    @Test
    void validateMonth() {
        int valid = 10;
        assertDoesNotThrow(() -> {service.validateMonth(valid);});
    }

    @Test
    void catchInvalidYear() {
        int invalid = 1000;
        Exception e = assertThrows(IllegalArgumentException.class, () -> {service.validateYear(invalid);});
        String expected = "Error: year expected within [1970, 2050]";
        assertEquals(expected, e.getMessage());
    }

    @Test
    void validateYear() {
        int valid = 2022;
        assertDoesNotThrow(() -> {service.validateYear(valid);});
    }

    @Test
    void catchInvalidPageSize() {
        int invalid = -24;
        Exception e = assertThrows(IllegalArgumentException.class, () -> {service.validatePageSize(invalid);});
        String expected = "Error: page_size expected within [1, 25]";
        assertEquals(expected, e.getMessage());
    }

    @Test
    void validatePageSize() {
        int valid = 10;
        assertDoesNotThrow(() -> {service.validatePageSize(valid);});
    }
}