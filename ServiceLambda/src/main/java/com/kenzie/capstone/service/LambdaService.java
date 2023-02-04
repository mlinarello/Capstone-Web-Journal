package com.kenzie.capstone.service;

import com.kenzie.capstone.service.dao.ExampleDao;
import com.kenzie.capstone.service.model.ExampleData;
import com.kenzie.capstone.service.model.ExampleRecord;
import com.kenzie.capstone.service.util.EmailClient;
import com.mailgun.model.message.MessageResponse;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;

public class LambdaService {

    private ExampleDao exampleDao;

    @Inject
    public LambdaService(ExampleDao exampleDao) {
        this.exampleDao = exampleDao;
    }

    public ExampleData getExampleData(String id) {
        List<ExampleRecord> records = exampleDao.getExampleData(id);
        if (records.size() > 0) {
            return new ExampleData(records.get(0).getId(), records.get(0).getData());
        }
        return null;
    }

    public ExampleData setExampleData(String data) {
        String id = UUID.randomUUID().toString();
        ExampleRecord record = exampleDao.setExampleData(id, data);
        return new ExampleData(id, data);
    }

    public MessageResponse sendWelcomeMessage(String email){
        EmailClient emailClient = new EmailClient();
        return emailClient.sendWelcomeMessage(email);
    }

}
