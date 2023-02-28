package com.s8.keeilzhanstd.challenge.utility.validation;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public void validate(int month, int year, int pageSize) {
        validateMonth(month);
        validateYear(year);
        validatePageSize(pageSize);
    }

    public void validateMonth(int month) throws IllegalArgumentException {
        if (1 <= month && month <= 12) {
            return;
        }

        throw new IllegalArgumentException("Error: month expected within [1, 12]");
    }

    public void validateYear(int year) throws IllegalArgumentException {
        if (year >= 1970 && year <= 2050) {
           return;
        }

        throw new IllegalArgumentException("Error: year expected within [1970, 2050]");
    }

    public void validatePageSize(int pageSize) throws IllegalArgumentException {
        if (pageSize > 0 && pageSize < 26) {
            return;
        }

        throw new IllegalArgumentException("Error: page_size expected within [1, 25]");
    }
}
