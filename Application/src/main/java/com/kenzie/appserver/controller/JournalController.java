package com.kenzie.appserver.controller;

import com.kenzie.appserver.controller.model.*;
import com.kenzie.appserver.service.JournalService;
import com.kenzie.appserver.service.model.Journal;
import com.kenzie.appserver.service.model.JournalEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalController {

    private JournalService journalService;

    public JournalController(JournalService journalService) {
        this.journalService = journalService;
    }

    @PostMapping("/create")
    public ResponseEntity<JournalResponse> createJournal(@RequestBody CreateJournalRequest createJournalRequest) {
        System.out.println("Hit journalController CreateJournal");
        Journal journal = new Journal(createJournalRequest.getUsername(),
                UUID.randomUUID().toString(),
                createJournalRequest.getJournalName(),
                LocalDateTime.now().toString());

        try {
            journalService.createJournal(journal);
        }
        catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().build();
        }

        JournalResponse createJournalResponse = createJournalResponse(journal);

        return ResponseEntity.ok(createJournalResponse);

    }

    @DeleteMapping("/{journalId}")
    public ResponseEntity<JournalResponse> deleteJournal(@PathVariable("journalId") String journalId) {
        Journal journalToDelete = journalService.getJournalById(journalId);

        if (journalToDelete == null) {
            return ResponseEntity.notFound().build();
        }

        try {
            journalService.deleteJournal(journalToDelete);
        }
        catch (IllegalArgumentException iae) {
            return ResponseEntity.badRequest().build();
        }

        JournalResponse response = createJournalResponse(journalToDelete);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{journalId}")
    public ResponseEntity<JournalResponse> getJournalByJournalId(@PathVariable("journalId") String journalId) {
        Journal journal = journalService.getJournalById(journalId);

        if (journal == null) {
            return ResponseEntity.notFound().build();
        }

        JournalResponse response = createJournalResponse(journal);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/all/{username}")
    public ResponseEntity<List<JournalResponse>> getAllJournalsForUser(@PathVariable("username") String username) {
        List<Journal> journalsForUser = journalService.getAllJournalsForUser(username);

        if (journalsForUser == null || journalsForUser.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<JournalResponse> responses = journalsForUser
                .stream()
                .map(this::createJournalResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/entry")
    public ResponseEntity<JournalEntryResponse> createJournalEntry(@RequestBody CreateEntryRequest createEntryRequest) {
        JournalEntry entry = new JournalEntry();
        entry.setEntryId(UUID.randomUUID().toString());
        entry.setJournalId(createEntryRequest.getJournalId());
        entry.setBody(createEntryRequest.getBody());
        entry.setTimestamp(LocalDateTime.now().toString());
        entry.setTitle(createEntryRequest.getTitle());
        entry.setUsername(createEntryRequest.getUsername());

        if (entry.getJournalId() == null || entry.getBody() == null ||
        entry.getTitle() == null || entry.getUsername() ==null) {
            return ResponseEntity.badRequest().build();
        }

        JournalEntry added = journalService.createJournalEntry(entry);

        return ResponseEntity.ok(createJournalEntryResponse(added));
    }

    @GetMapping("/entry/all/{journalId}")
    public ResponseEntity<List<JournalEntryResponse>> getAllEntryForJournal(@PathVariable("journalId") String journalId) {
        List<JournalEntry> entries = journalService.getEntriesForJournal(journalId);

        List<JournalEntryResponse> responses = new ArrayList<>();

        for (JournalEntry e : entries) {
            responses.add(createJournalEntryResponse(e));
        }

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/entry/{journalDeleteKey}")
    public ResponseEntity<String> deleteEntryByJournalEntryId(@PathVariable("journalDeleteKey") String journalDeleteKey) {
        journalService.deleteJournalEntry(journalDeleteKey);
        return ResponseEntity.ok().build();
    }

    private JournalResponse createJournalResponse(Journal journal) {
        JournalResponse response = new JournalResponse();

        response.setJournalId(journal.getJournalId());
        response.setJournalName(journal.getJournalName());
        response.setUsername(journal.getUsername());
        response.setDateCreated(journal.getDateCreated());
        return response;
    }

    private JournalEntryResponse createJournalEntryResponse(JournalEntry entry) {
        JournalEntryResponse response = new JournalEntryResponse();

        response.setJournalId(entry.getJournalId());
        response.setJournalEntryId(entry.getEntryId());
        response.setUsername(entry.getUsername());
        response.setTimestamp(entry.getTimestamp());
        response.setBody(entry.getBody());
        response.setTitle(entry.getTitle());

        return response;
    }
}
