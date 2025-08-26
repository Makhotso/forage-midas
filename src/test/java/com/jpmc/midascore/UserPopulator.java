package com.jpmc.midascore;

import com.jpmc.midascore.component.DatabaseConduit;
import com.jpmc.midascore.entity.UserRecord;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.jpmc.midascore.repository.UserRecordRepository;


@Component
public class UserPopulator {
    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private DatabaseConduit databaseConduit;

    @Autowired
    private UserRecordRepository repository;

    @PostConstruct
    public void init() {
        populate();
    }

    public void populate() {
        String[] userLines = fileLoader.loadStrings("/test_data/lkjhgfdsa.hjkl");
        for (String userLine : userLines) {
            String[] userData = userLine.split(", ");
            UserRecord user = new UserRecord(userData[0], Float.parseFloat(userData[1]));
            databaseConduit.save(user);
        }

        /*UserRecord u = new UserRecord("waldorf", 1000);  // ✅ entity object
        repository.save(u); hard code test before I could load DB*/
    }
    public UserRecord findByName(String name) {
        return repository.findByName("waldorf");

    }
}



