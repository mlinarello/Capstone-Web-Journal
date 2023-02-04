package com.kenzie.appserver.repositories.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "User")
public class UserRecord {
    private String username;
    private String name;
    private String password;
    private String email;
    //string

    @DynamoDBHashKey(attributeName = "username")
    public String getUsername() {
        return this.username;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return this.name;
    }

    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return this.password;
    }

    @DynamoDBAttribute(attributeName = "email")
    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserRecord that = (UserRecord) o;

        if (!Objects.equals(username, that.username)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(password, that.password)) return false;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}

