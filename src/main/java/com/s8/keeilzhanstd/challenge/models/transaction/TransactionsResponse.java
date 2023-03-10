package com.s8.keeilzhanstd.challenge.models.transaction;

import lombok.Data;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionsResponse {
    private int pages_amount = 0;
    private String base_currency;
    private List<Page> pages = new ArrayList<>();

    public TransactionsResponse(List<Transaction> transactions, int pageSize, String rates) {

        // Base currency from ratesApi provider https://www.exchangerate-api.com/docs/overview
        this.base_currency = new JSONObject(rates).get("base_code").toString();

        List<Transaction> tba_transactions = new ArrayList<>();

        for(Transaction t: transactions) {
            if(tba_transactions.size() < pageSize) {
                // Append transactions to page until transactions != pageSize
                tba_transactions.add(t);
            }

            if(tba_transactions.size() == pageSize) {
                // Add page to a pageList in Transaction response, then clear the page
                pages.add(new Page(tba_transactions, ++pages_amount, rates));
                tba_transactions = new ArrayList<>();
            }
        }

        // Add the rest of the transactions that did not get into page due to transactions.size() < pageSize
        // E.g. 23 transaction goes into 2p with 10t, and 1p with 3t.

        if(!tba_transactions.isEmpty()) {
            pages.add(new Page(tba_transactions, ++pages_amount, rates));
        }
    }
}