package com.kenzie.appserver.service.model;

public class Journal {

    private final String username;
    private final String journalId;
    private final String journalName;
    private final String dateCreated;

    public Journal(String username, String journalId, String journalName, String dateCreated) {
        this.username = username;
        this.journalId = journalId;
        this.journalName = journalName;
        this.dateCreated = dateCreated;
    }

    public String getUsername() {
        return username;
    }

    public String getJournalId() {
        return journalId;
    }

    public String getJournalName() {
        return journalName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    @Override
    public String toString() {
        return "Journal{" +
                "username='" + username + '\'' +
                ", journalId='" + journalId + '\'' +
                ", journalName='" + journalName + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                '}';
    }
}

