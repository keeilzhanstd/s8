package com.s8.keeilzhanstd.challenge.controllers;

import com.s8.keeilzhanstd.challenge.annotations.Loggable;
import com.s8.keeilzhanstd.challenge.models.transaction.Transaction;
import com.s8.keeilzhanstd.challenge.services.TransactionService;
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
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error message: e.getMessage()"
                    )
            }
    )
    public ResponseEntity<?> publishTransaction(@RequestBody Transaction transaction, Principal principal)
    {
        try {
            return ResponseEntity.ok(service.publish(transaction, principal.getName()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping
    @Loggable
    @Operation(
            description = "Retrieve a transactions for a particular month from kafka",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved transactions from kafka"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error message: e.getMessage()"
                    )
            }
    )
    public ResponseEntity<?> consumeTransactions(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam String currency,
            Principal principal) {
        try{
            return ResponseEntity.ok(service.consume(principal.getName(), month, year, pageSize, currency));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

}

