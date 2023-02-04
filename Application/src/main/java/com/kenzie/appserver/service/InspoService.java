package com.kenzie.appserver.service;

import com.kenzie.appserver.controller.model.Quote;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class InspoService {
    List<Quote> quotes;

    public InspoService() {
        quotes = new ArrayList<>();
    }

    public Quote getInspirationalQuote() {
        List<String> quoteStrings;
        List<Quote> quotes;
        try {
            Scanner scanner = new Scanner(new File("quotes.csv"));
            quoteStrings = new ArrayList<>();
            quotes = new ArrayList<>();
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                quoteStrings.add(scanner.next());
            }
            for (String s : quoteStrings) {
                String[] authorAndQuotes = s.split("\",\"");
                Quote quote = new Quote(authorAndQuotes[0].substring(1), authorAndQuotes[1].substring(0, authorAndQuotes[1].length() - 1));
                if (quote.hasAuthor()) {
                    quotes.add(quote);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return quotes.get((int) ((Math.random() * (quotes.size() - 2)) + 2));
    }
}
