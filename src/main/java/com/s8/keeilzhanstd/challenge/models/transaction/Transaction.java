package com.s8.keeilzhanstd.challenge.models.transaction;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class Transaction implements Serializable {
    private String uuid;

    @NotNull
    private Double amount;

    @NotNull
    private String currency;

    @NotNull
    private String iban;

    @NotNull
    private Date date;

    @NotNull
    private String description;
}
