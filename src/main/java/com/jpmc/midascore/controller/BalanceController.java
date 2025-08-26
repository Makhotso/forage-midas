package com.jpmc.midascore.controller;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRecordRepository;
import com.jpmc.midascore.foundation.Balance;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class BalanceController {

    private final UserRecordRepository userRepo;

    public BalanceController(UserRecordRepository userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/balance")
    public Balance getBalance(@RequestParam long userId) {
        Optional<UserRecord> userOpt = userRepo.findById(userId);

        float balance = userOpt.map(UserRecord::getBalance).orElse(0.0f);

        return new Balance(userId, balance);
    }
}
