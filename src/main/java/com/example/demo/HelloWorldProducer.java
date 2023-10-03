package com.example.demo;

import java.time.Instant;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kafkainaction.Alert;

import static org.kafkainaction.AlertStatus.Critical;

public class HelloWorldProducer {
    private static final Logger logger = LogManager.getLogger();
    public static void main(String[] args) {
        Properties kaProperties = new Properties();
        kaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        kaProperties.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        kaProperties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        kaProperties.put("schema.registry.url", "http://localhost:8081");
        try (Producer<Long, Alert> producer = new KafkaProducer<>(kaProperties)){
            Alert alert = new Alert(12345L, Instant.now().toEpochMilli(), Critical);
            logger.info("kinaction_info Alert -> {}", alert);
            ProducerRecord<Long, Alert> producerRecord = new ProducerRecord<>("kinaction_schematest", alert.getSensorId(), alert);           
            producer.send(producerRecord);
            logger.info("closing producer");
        }

    }
}