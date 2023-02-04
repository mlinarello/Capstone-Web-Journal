package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

public class JournalEntryKey {

    private String journalId, entryId;

    public JournalEntryKey(String journalId, String entryId) {
        this.journalId = journalId;
        this.entryId = entryId;
    }

    public JournalEntryKey() {
    }

    @DynamoDBHashKey(attributeName = "journalId")
    public String getJournalId() {
        return journalId;
    }

    public void setJournalId(String journalId) {
        this.journalId = journalId;
    }

    @DynamoDBRangeKey(attributeName = "entryId")
    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }
}
