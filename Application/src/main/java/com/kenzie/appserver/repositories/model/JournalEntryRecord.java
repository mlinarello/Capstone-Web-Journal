package com.kenzie.appserver.repositories.model;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = "JournalEntry")
public class JournalEntryRecord {

    @Id
    private JournalEntryKey journalEntryKey;
    private String journalId;
    private String entryId;
    private String timestamp;

    private String username;
    private String title;
    private String body;


    @DynamoDBHashKey(attributeName = "journalId")
    public String getJournalId() {
        return this.journalEntryKey.getJournalId();
    }

    public void setJournalId(String journalId) {
        if (journalEntryKey == null) {
            this.journalEntryKey = new JournalEntryKey(journalId, null);
        }
        journalEntryKey.setJournalId(journalId);
    }


    @DynamoDBRangeKey(attributeName = "entryId")
    public String getEntryId() {
        return journalEntryKey.getEntryId();
    }

    public void setEntryId(String entryId) {
        if (journalEntryKey == null) {
            this.journalEntryKey = new JournalEntryKey(null, entryId);
        }
        journalEntryKey.setEntryId(entryId);
    }

    @DynamoDBAttribute(attributeName = "timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @DynamoDBAttribute(attributeName = "username")
    public String getUsername() {
        return username;
    }

    public void setusername(String username) {
        this.username = username;
    }
    @DynamoDBAttribute(attributeName = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute(attributeName = "body")
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
