package com.example.demo;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kafkainaction.Alert;

public class HelloWorldConsumer {
    final static Logger logger = LogManager.getLogger();
    private volatile boolean keepConsuming = true;

    public static void main(String[] args) {
        Properties kaProperties = new Properties();
        kaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        kaProperties.put("group.id", "kinaction_helloconsumer");
        kaProperties.put("enable.auto.commit", "true");
        kaProperties.put("auto.commit.interval.ms", "1000");
        kaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        kaProperties.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        kaProperties.put("schema.registry.url", "http://localhost:8081");
        HelloWorldConsumer helloWorldConsumer = new HelloWorldConsumer();
        Runtime.getRuntime().addShutdownHook(new Thread(helloWorldConsumer::shutdown));
        helloWorldConsumer.consume(kaProperties);
    }

    private void consume(Properties kaProperties) {
        try (KafkaConsumer<Long, Alert> consumer = new KafkaConsumer<>(kaProperties)) {
            consumer.subscribe(List.of("kinaction_schematest"));
            while (keepConsuming) {
                ConsumerRecords<Long, Alert> records = consumer.poll(Duration.ofMillis(250));
                for (ConsumerRecord<Long, Alert> record : records) {
                    logger.info("kinaction_info Alert -> {}", record.value());
                    logger.info("kinaction_info offset = {}, kinaction_value = {}", record.offset(), record.value());
                }
            }
        }
    }

    private void shutdown() {
        System.out.println("Running Shutdown Hook");
        keepConsuming = false;
    }
}