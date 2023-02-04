package com.kenzie.appserver.controller;


import com.kenzie.appserver.controller.model.Quote;
import com.kenzie.appserver.service.InspoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inspiration")
public class InspoController {
    private InspoService inspoService;

    public InspoController(InspoService inspoService) {
        this.inspoService = inspoService;
    }

    @GetMapping
    public ResponseEntity<Quote> getInspirationalQuote(){
        Quote inspirationalQuote = inspoService.getInspirationalQuote();
        return ResponseEntity.ok(inspirationalQuote);
    }
}
