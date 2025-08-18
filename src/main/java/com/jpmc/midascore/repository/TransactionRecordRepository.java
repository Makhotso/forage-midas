package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.TransactionRecord;
import org.springframework.data.repository.CrudRepository;
import com.jpmc.midascore.entity.UserRecord;

public interface TransactionRecordRepository extends CrudRepository<TransactionRecord, Long> {
    /*UserRecord findByName(String name);*/
}