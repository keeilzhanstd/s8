//// https://github.com/testcontainers/testcontainers-java/blob/main/modules/kafka/src/test/java/org/testcontainers/containers/KafkaContainerTest.java#L28
//
//package com.s8.keeilzhanstd.challenge.testcontainers;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.s8.keeilzhanstd.challenge.models.transaction.Transaction;
//import com.s8.keeilzhanstd.challenge.services.BaseServiceTest;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.common.TopicPartition;
//import org.junit.jupiter.api.Test;
//import org.testcontainers.containers.KafkaContainer;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.tuple;
//
//class KafkaTestcontainersTest extends BaseServiceTest {
//
//    @Test
//    public void testUsageLocal() throws Exception {
//        try (KafkaContainer kafka = new KafkaContainer(KAFKA_TEST_IMAGE)) {
//            kafka.start();
//            testKafkaFunctionalityLocal(kafka.getBootstrapServers());
//        }
//    }
//
//    protected void testKafkaFunctionalityLocal(String bootstrapServers) throws Exception {
//
//        Transaction transaction = new Transaction(UUID.randomUUID().toString(), 1000.0, "HKD", "HK93-0000-0000-0000-0021-1", new Date(), "HKD Withdrawal");
//
//        int year = 2020;
//        int month = 1;
//        String userKey = "P000-TEST";
//
//        String topic = "transactions-Y" + year + "-" + userKey;
//
//        try {
//            // Prepare Transaction for a StringSerializer
//            String topicTransaction = new ObjectMapper().writeValueAsString(transaction);
//            kafkaTemplate.send(topic, month, transaction.getUuid(), topicTransaction);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//
//        ObjectMapper mapper = new ObjectMapper();
//        List<Transaction> results = new ArrayList<>();
//        ConsumerRecords<String, String> data;
//
//        // Consumer
//        TopicPartition tp = new TopicPartition(topic, month - 1);
//
//        // Subscribe consumer to a kafka topic.
//        consumer.assign(List.of(tp));
//        consumer.seek(tp, 0);
//        do {
//            data = consumer.poll(Duration.ofMillis(500));
//            for (ConsumerRecord<String, String> record : data.records(topic)) {
//                try {
//                    Transaction t = mapper.readValue(record.value(), Transaction.class);
//                    System.out.println(t);
//                    results.add(t);
//                } catch (JsonProcessingException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        } while (!data.isEmpty());
//
//        consumer.unsubscribe();
//        //assert transaction equals t
//        assertThat(results).extracting("uuid", "amount", "currency", "iban", "date", "description")
//                .containsExactly(tuple(transaction.getUuid(), transaction.getAmount(), transaction.getCurrency(), transaction.getIban(), transaction.getDate(), transaction.getDescription()));
//
//    }
//}
