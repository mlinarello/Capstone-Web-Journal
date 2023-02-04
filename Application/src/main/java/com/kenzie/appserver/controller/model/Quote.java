package com.kenzie.appserver.controller.model;

public class Quote {
    private String author, body;

    public Quote() {
    }

    public Quote(String author, String body) {
        this.author = author;
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean hasAuthor(){
        return !getAuthor().isEmpty();
    }

    @Override
    public String toString() {
        return "Quote{" +
                "author='" + author + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
