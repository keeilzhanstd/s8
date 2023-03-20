package com.s8.keeilzhanstd.challenge.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s8.keeilzhanstd.challenge.annotations.Loggable;
import com.s8.keeilzhanstd.challenge.models.transaction.Page;
import com.s8.keeilzhanstd.challenge.models.transaction.Transaction;
import com.s8.keeilzhanstd.challenge.models.transaction.TransactionsResponse;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TransactionService {
    // Logger
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);

    private final ValidationService validationService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Consumer<String, String> consumer;
    private final FxRatesService fxService;

    @Loggable
    public Transaction publish(Transaction transaction, String userKey) {
        // Add random uuid for each transaction
        transaction.setUuid(UUID.randomUUID().toString());

        Calendar cal = Calendar.getInstance();
        cal.setTime(transaction.getDate());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);

        String topic = "transactions-Y"+year+"-"+userKey;

        try {
            // Prepare Transaction for a StringSerializer
            String topicTransaction = new ObjectMapper().writeValueAsString(transaction);
            kafkaTemplate.send(topic, month, transaction.getUuid(), topicTransaction);

            if(log.isDebugEnabled()){
                log.debug("TransactionService.publish() published transaction: {}", topicTransaction);
            }

            return transaction;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Loggable
    public TransactionsResponse consume(String userKey, int month, int year, int pageSize, String currency) {

        // Validate user query params
        validationService.validate(month, year, pageSize);

        // Fetch latest exchange rates
        String rates;
        try {
            rates = fxService.getLatestRates(currency);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Retrieve list of transactions for specified (month, year) by user_key (@username) from Kafka
        List<Transaction> transactions = getTransactionsListByUserKey(month, year, userKey);

        List<Transaction> tba_transactions = new ArrayList<>();
        List<Page> pages = new ArrayList<>();
        int pages_amount = 0;

        for(Transaction t: transactions) {

            // Append transactions to page until transactions != pageSize
            if(tba_transactions.size() < pageSize) {
                tba_transactions.add(t);
            }

            // Build page and add it to pages list if transactions.size() == pageSize
            if(tba_transactions.size() == pageSize) {
                pages.add( Page.builder()
                        .transactions(tba_transactions)
                        .pageNumber(++pages_amount)
                        .creditDebit(calculateRates(rates, tba_transactions))
                        .build());
                tba_transactions = new ArrayList<>();
            }
        }

        // Add the rest of the transactions that did not get into page due to transactions.size() < pageSize
        // E.g. 23 transaction goes into 2p with 10t, and 1p with 3t.
        if(!tba_transactions.isEmpty()) {
            pages.add( Page.builder()
                    .transactions(tba_transactions)
                    .pageNumber(++pages_amount)
                    .creditDebit(calculateRates(rates, tba_transactions))
                    .build());
        }

        // Return TransactionResponse
        return TransactionsResponse.builder()
                .pages(pages)
                .pages_amount(pages_amount)
                .base_currency(currency)
                .build();
    }

    private List<Transaction> getTransactionsListByUserKey(int month, int year, String userKey) {
        ObjectMapper mapper = new ObjectMapper();
        List<Transaction> results = new ArrayList<>();
        ConsumerRecords<String, String> data;

        String topic = "transactions-Y"+year+"-"+userKey;
        TopicPartition tp = new TopicPartition(topic, month - 1);

        // Subscribe consumer to a kafka topic.
        consumer.assign(List.of(tp));
        consumer.seek(tp, 0);
        do {
            data = consumer.poll(Duration.ofMillis(500));
            for (ConsumerRecord<String, String> record : data.records(topic)) {
                try {
                    Transaction t = mapper.readValue(record.value(), Transaction.class);
                    results.add(t);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        } while (!data.isEmpty());

        // Unsubscribe consumer
        consumer.unsubscribe();

        if(log.isDebugEnabled()){
            log.debug("TransactionService.getTransactionsListByUserKey() retrieved transactions: {}", results);
        }

        return results;
    }

    private double calculateRates(String rates, List<Transaction> tba_transactions){

        /* Example rates
        ...
        "base_code": "USD",
        "conversion_rates": {
        "USD": 1,
        "AED": 3.6725,
        "AFN": 87.7149,
        "ALL": 107.6744,
        "AMD": 388.4595,
        "ANG": 1.79,
        "AOA": 511.696,
        ...
         */

        JSONObject obj = new JSONObject(rates);
        double state = 0;
        for(Transaction transaction: tba_transactions){
            double rate = obj.getJSONObject("conversion_rates").getDouble(transaction.getCurrency());
            state += transaction.getAmount() / rate;
        }

        return state;
    }

}
