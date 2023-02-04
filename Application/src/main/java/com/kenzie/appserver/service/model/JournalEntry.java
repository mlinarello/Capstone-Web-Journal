package com.kenzie.appserver.service.model;

public class JournalEntry {
    private String journalId, journalEntryId, timestamp, username, title, body;
    private boolean isFavorite;

    public JournalEntry() {
    }

    public JournalEntry(String journalId, String journalEntryId, String timestamp, String username, String title, String body) {
        this.journalId = journalId;
        this.journalEntryId = journalEntryId;
        this.timestamp = timestamp;
        this.username = username;
        this.title = title;
        this.body = body;
    }

    public String getJournalId() {
        return journalId;
    }

    public void setJournalId(String journalId) {
        this.journalId = journalId;
    }

    public String getEntryId() {
        return journalEntryId;
    }

    public void setEntryId(String journalEntryId) {
        this.journalEntryId = journalEntryId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
