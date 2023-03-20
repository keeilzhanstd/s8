package com.s8.keeilzhanstd.challenge.models.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionsResponse {

    @NotNull
    private int pages_amount;

    @NotNull
    private String base_currency;

    @NotNull
    private List<Page> pages;

}