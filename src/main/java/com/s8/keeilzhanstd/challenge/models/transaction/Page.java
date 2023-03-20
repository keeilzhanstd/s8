package com.s8.keeilzhanstd.challenge.models.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Page {
    @NotNull
    private List<Transaction> transactions;
    @NotNull
    private int pageNumber;
    @NotNull
    private double creditDebit;
}
