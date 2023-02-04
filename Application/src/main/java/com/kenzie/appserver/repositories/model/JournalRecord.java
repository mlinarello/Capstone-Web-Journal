package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = "Journal")
public class JournalRecord {
    @Id
    private JournalKey journalKey;
    private String journalName;
    private String isFavorite;
    private String dateCreated;

    public JournalRecord() {
    }

    public JournalRecord(JournalKey journalKey, String journalName, String dateCreated) {
        this.journalKey = journalKey;
        this.journalName = journalName;
        this.dateCreated = dateCreated;
    }

    @DynamoDBHashKey(attributeName = "username")
    public String getUsername() {
        return journalKey.getUsername();
    }

    @DynamoDBRangeKey(attributeName = "journalId")
    public String getJournalId() {
        return journalKey.getJournalId();
    }

    @DynamoDBAttribute(attributeName = "journalName")
    public String getJournalName() {
        return journalName;
    }

    @DynamoDBAttribute(attributeName = "dateCreated")
    public String getDateCreated() {
        return dateCreated;
    }

    public void setUsername(String username) {
        if (journalKey == null) {
            this.journalKey = new JournalKey(username, null);
        }
        journalKey.setUsername(username);
    }

    public void setJournalId(String journalId) {
        if (journalKey == null) {
            this.journalKey = new JournalKey(null, journalId);
        }
        journalKey.setJournalId(journalId);
    }

    public void setJournalName(String journalName) {
        this.journalName = journalName;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }


}

