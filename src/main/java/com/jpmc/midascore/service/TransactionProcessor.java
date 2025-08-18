package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionProcessor {

    private final UserRecordRepository userRepo;
    private final TransactionRecordRepository txRepo;

    public TransactionProcessor(UserRecordRepository userRepo,
                                TransactionRecordRepository txRepo) {
        this.userRepo = userRepo;
        this.txRepo = txRepo;
    }

    @Transactional
    public void process(Transaction tx) {
        Optional<UserRecord> senderOpt = userRepo.findById(tx.getSenderId());
        Optional<UserRecord> recipientOpt = userRepo.findById(tx.getRecipientId());

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) {
            return; // invalid IDs → discard
        }

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        if (sender.getBalance() < tx.getAmount()) {
            return; // insufficient funds → discard
        }

        // Adjust balances
        sender.setBalance(sender.getBalance() - tx.getAmount());
        recipient.setBalance(recipient.getBalance() + tx.getAmount());

        // Persist updates
        userRepo.save(sender);
        userRepo.save(recipient);

        // Persist transaction record
        TransactionRecord record = new TransactionRecord(sender, recipient, tx.getAmount());
        txRepo.save(record);
    }
}