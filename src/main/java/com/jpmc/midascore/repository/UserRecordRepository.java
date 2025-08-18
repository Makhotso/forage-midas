package com.jpmc.midascore.repository;

import com.jpmc.midascore.entity.UserRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecordRepository extends CrudRepository<UserRecord, Long> {
    // Custom query method to find a user by name
    UserRecord findByName(String name);
}
