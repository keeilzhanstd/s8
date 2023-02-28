package com.s8.keeilzhanstd.challenge.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.s8.keeilzhanstd.challenge.annotations.Loggable;
import com.s8.keeilzhanstd.challenge.models.transaction.Transaction;
import com.s8.keeilzhanstd.challenge.models.transaction.TransactionsResponse;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TransactionService {
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

        // Retrieve list of transactions for specified month by user_key (@username)
        List<Transaction> transactions = getTransactionsListByUserKey(consumer, month, year, userKey);

        return new TransactionsResponse(transactions, pageSize, rates);
    }

    private List<Transaction> getTransactionsListByUserKey(Consumer<String, String> consumer, int month, int year, String userKey) {
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
        return results;
    }

}
