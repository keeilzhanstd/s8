package com.s8.keeilzhanstd.challenge.models.transaction;

import lombok.Data;
import org.json.JSONObject;

import java.util.List;

@Data
public class Page {

    private final List<Transaction> transactions;
    private final int pageNumber;
    private double credit = 0f;
    private double debit = 0f;

    Page(List<Transaction> transactions, int pageNumber, String rates) {
        this.transactions = transactions;
        this.pageNumber = pageNumber;
        this.calculate(rates);
    }

    private void calculate(String rates) {
        // Calculate debit/credit for a page based on the current rates.
        JSONObject obj = new JSONObject(rates);
        for(Transaction transaction: transactions){
            double rate = obj.getJSONObject("conversion_rates").getDouble(transaction.getCurrency());
            if(transaction.getAmount() <= 0f)
                credit += transaction.getAmount() / rate;
            else
                debit += transaction.getAmount() / rate;
        }
    };
}
