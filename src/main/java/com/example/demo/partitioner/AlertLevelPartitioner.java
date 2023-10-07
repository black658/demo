package com.example.demo.partitioner;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import com.example.demo.model.Alert;

public class AlertLevelPartitioner implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int criticalLevelPartition = findCriticalPartitionNumber(cluster, topic);
        return isCriticalLevel(((Alert)key).getAlertLevel()) ? criticalLevelPartition : findRandomPartition(cluster, topic, key);
    }

    public int findCriticalPartitionNumber(Cluster cluster, String topic) {
        return 0;
    }

    public boolean isCriticalLevel(String level) {
        if (level.toUpperCase().contains("CRITICAL")) {
            return true;
        } else {
            return false;
        }
    }

    public int findRandomPartition(Cluster cluster, String topic, Object key) {
        List<PartitionInfo> partitionMetaList = cluster.availablePartitionsForTopic(topic);
        Random randomPart = new Random();
        return randomPart.nextInt(partitionMetaList.size());
    }  

    @Override
    public void configure(Map<String, ?> configs) {
    }

    @Override
    public void close() {
    }
    
}
