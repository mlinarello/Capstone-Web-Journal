package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import org.springframework.context.annotation.Bean;


public class JournalKey {

    private String username, journalId;
    public JournalKey(String username, String journalId) {
        this.username = username;
        this.journalId = journalId;
    }

    public JournalKey() {
    }

    public String getUsername() {
        return username;
    }

    @DynamoDBHashKey(attributeName = "username")
    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBRangeKey(attributeName = "journalId")
    public String getJournalId() {
        return journalId;
    }

    public void setJournalId(String journalId) {
        this.journalId = journalId;
    }
}
