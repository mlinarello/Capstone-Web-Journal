package com.kenzie.appserver.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;

public class JournalResponse {

    @NotEmpty
    @JsonProperty("username")
    private String username;

    @NotEmpty
    @JsonProperty("journalId")
    private String journalId;

    @NotEmpty
    @JsonProperty("journalName")
    private String journalName;

    @NotEmpty
    @JsonProperty("dateCreated")
    private String datecreated;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getJournalId() {
        return journalId;
    }

    public void setJournalId(String journalId) {
        this.journalId = journalId;
    }

    public String getJournalName() {
        return journalName;
    }

    public void setJournalName(String journalName) {
        this.journalName = journalName;
    }

    public String getDateCreated() {
        return datecreated;
    }

    public void setDateCreated(String datecreated) {
        this.datecreated = datecreated;
    }
}
