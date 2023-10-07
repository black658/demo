package com.example.demo.callback;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AlertCallback implements Callback{
    private static final Logger logger = LogManager.getLogger();

    @Override
    public void onCompletion(RecordMetadata metadata, Exception exception) {
        if (exception != null) {
            logger.error("kinaction_error", exception);
        } else {
            logger.info("kinaction_info offset = {}, topic = {}, timestamp = {}", metadata.offset(), metadata.topic(), metadata.timestamp());
        }
    }
    
}
