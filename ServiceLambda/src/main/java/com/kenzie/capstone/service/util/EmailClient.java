package com.kenzie.capstone.service.util;


import com.mailgun.api.v3.MailgunMessagesApi;
import com.mailgun.client.MailgunClient;
import com.mailgun.model.message.Message;
import com.mailgun.model.message.MessageResponse;

public class EmailClient {
    private String API_KEY = "REDACTED";
    private String YOUR_DOMAIN_NAME = "sandboxd904e621b7b141aaa1f0e7965d7a8522.mailgun.org";

    private MailgunMessagesApi mailgunMessagesApi;

    public EmailClient() {
        mailgunMessagesApi = MailgunClient.config(API_KEY)
                .createApi(MailgunMessagesApi.class);
    }

    public MessageResponse sendSimpleMessage() {
        Message message = Message.builder()
                .from("MNDT Journal <USER@" + YOUR_DOMAIN_NAME + ">")
                .to("mlinarello25@gmail.com")
                .subject("Testing")
                .text("Testing out some Mailgun awesomeness!")
                .build();

        return mailgunMessagesApi.sendMessage(YOUR_DOMAIN_NAME, message);
    }

    public MessageResponse sendWelcomeMessage(String email) {
        System.out.println("sendWelcomeMessage in EmailClient.java");
        String welcomeMessage = "Welcome " + email + "!\n\n" +
                "Thank you for joining MNDT Journal!\n" +
                "Journaling can help you reduce stress, decrease anxiety and overall leave you feeling better for it.\n" +
                "Journaling also helps you remember your life as you look back on the entries, which helps time" +
                " not feel like it goes so fast.\n\n" +
                "We're glad you've decided to join us!" +
                "\n - MNDT Dev Team";
        Message message = Message.builder()
                .from("MNDT Journal <USER@" + YOUR_DOMAIN_NAME + ">")
                .to(email)
                .subject("Welcome to MNDT Journal!")
                .text(welcomeMessage)
                .build();
        return mailgunMessagesApi.sendMessage(YOUR_DOMAIN_NAME, message);
    }
}
