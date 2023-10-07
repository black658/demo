package com.example.demo.producer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.demo.model.Alert;
import com.example.demo.serde.AlertKeySerde;

public class AlertTrendingProducer {

  private static final Logger log = LogManager.getLogger(AlertTrendingProducer.class);

  public static void main(String[] args) throws InterruptedException, ExecutionException {

    Properties kaProperties = new Properties();
    kaProperties.put("bootstrap.servers",
                           "localhost:9092,localhost:9093,localhost:9094");
    kaProperties.put("key.serializer",
                          AlertKeySerde.class.getName());   //<1>
    kaProperties.put("value.serializer",
                           "org.apache.kafka.common.serialization.StringSerializer");

    try (Producer<Alert, String> producer = new KafkaProducer<>(kaProperties)) {
      Alert alert = new Alert(0, "Stage 0", "CRITICAL", "Stage 0 stopped");
      ProducerRecord<Alert, String> producerRecord =
          new ProducerRecord<>("kinaction_alerttrend", alert, alert.getAlertMessage());    //<2>

      RecordMetadata result = producer.send(producerRecord).get();
      log.info("kinaction_info offset = {}, topic = {}, timestamp = {}",
               result.offset(), result.topic(), result.timestamp());
    }
  }
}
