package com.s8.keeilzhanstd.challenge.controllers;

import com.s8.keeilzhanstd.challenge.annotations.Loggable;
import com.s8.keeilzhanstd.challenge.services.TransactionService;
import com.s8.keeilzhanstd.challenge.models.transaction.Transaction;
import com.s8.keeilzhanstd.challenge.models.transaction.TransactionsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping("api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping
    @Loggable
    @Operation(
            description = "Publish a transaction to kafka",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully published a transaction!"
                    )
            }
    )
    public ResponseEntity<Transaction> publishTransaction(@RequestBody Transaction transaction, Principal principal)
    {
        return ResponseEntity.ok(service.publish(transaction, principal.getName()));
    }

    @GetMapping
    @Loggable
    @Operation(
            description = "Retrieve a transactions for a particular month from kafka",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved transactions from kafka"
                    )
            }
    )
    public ResponseEntity<TransactionsResponse> consumeTransactions(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam String currency,
            Principal principal) {
        return ResponseEntity.ok(service.consume(principal.getName(), month, year, pageSize, currency));
    }

}

