package com.jpmc.midascore.kafka;

import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionProcessor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TransactionListener {

    private final TransactionProcessor transactionProcessor;

    public TransactionListener(TransactionProcessor transactionProcessor) {
        this.transactionProcessor = transactionProcessor;
    }

    @KafkaListener(
            topics = "${general.kafka-topic}",
            groupId = "midas-core-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listen(Transaction transaction) {System.out.println("✅ Received Transaction: " + transaction);
    }
}
