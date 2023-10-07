package com.example.demo.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AuditProducer {
    private static final Logger logger = LogManager.getLogger();
      public static void main(String[] args) throws InterruptedException, ExecutionException {

    Properties kaProperties = new Properties();    //<1>
    kaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093");
    kaProperties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    kaProperties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    kaProperties.put("acks", "all");   //<2>
    kaProperties.put("retries", "3");    //<3>
    kaProperties.put("max.in.flight.requests.per.connection", "1");

    try (Producer<String, String> producer = new KafkaProducer<>(kaProperties)) {
      ProducerRecord<String, String> producerRecord = new ProducerRecord<>("kinaction_audit", null,
                                                                           "audit event");

      RecordMetadata result = producer.send(producerRecord).get();
      logger.info("kinaction_info offset = {}, topic = {}, timestamp = {}", result.offset(), result.topic(), result.timestamp());

    }
  }
}
