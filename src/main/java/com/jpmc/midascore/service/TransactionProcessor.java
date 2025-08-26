package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import com.jpmc.midascore.foundation.Incentive;

import java.util.Optional;

@Service
public class TransactionProcessor {

    private final UserRecordRepository userRepo;
    private final TransactionRecordRepository txRepo;
    private final RestTemplate restTemplate;

    private static final String INCENTIVE_API = "http://localhost:8080/incentive";

    public TransactionProcessor(UserRecordRepository userRepo,
                                TransactionRecordRepository txRepo,
                                RestTemplate restTemplate) {
        this.userRepo = userRepo;
        this.txRepo = txRepo;
        this.restTemplate = restTemplate;
    }

    private float fetchIncentive(Transaction tx) {
        Incentive incentive = restTemplate.postForObject(INCENTIVE_API, tx, Incentive.class);
        return incentive != null ? incentive.getAmount() : (float)0.0;
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

        //fetching incentive for this transaction
        float incentiveAmount = fetchIncentive(tx);

        // Adjust balances
        /*sender.setBalance(sender.getBalance() - tx.getAmount());
        recipient.setBalance(recipient.getBalance() + tx.getAmount());*/
        sender.setBalance(sender.getBalance() - (float) tx.getAmount());
        recipient.setBalance(recipient.getBalance() + (float) tx.getAmount() + incentiveAmount);

        // Persist updates
        userRepo.save(sender);
        userRepo.save(recipient);

        // Persist transaction record
        TransactionRecord record = new TransactionRecord(sender, recipient, tx.getAmount());
        txRepo.save(record);
    }
}