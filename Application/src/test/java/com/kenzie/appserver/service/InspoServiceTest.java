package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.Quote;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InspoServiceTest {
    private InspoService inspoService;

    @BeforeEach
    void setup(){
        inspoService = new InspoService();
    }

    @Test
    void getInspirationalQuote(){
        Quote quote = inspoService.getInspirationalQuote();
        System.out.println(quote.toString());
        Assertions.assertNotNull(quote);
    }
}
