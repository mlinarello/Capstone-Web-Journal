package com.kenzie.appserver.service;


import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.repositories.JournalEntryRepository;
import com.kenzie.appserver.repositories.JournalRepository;
import com.kenzie.appserver.repositories.model.JournalEntryKey;
import com.kenzie.appserver.repositories.model.JournalEntryRecord;
import com.kenzie.appserver.repositories.model.JournalKey;
import com.kenzie.appserver.repositories.model.JournalRecord;
import com.kenzie.appserver.service.model.Journal;
import com.kenzie.appserver.service.model.JournalEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class JournalService {

    private final JournalRepository journalRepository;
    private final JournalEntryRepository journalEntryRepository;
    private CacheStore cache;

    @Autowired
    public JournalService(JournalRepository journalRepository, JournalEntryRepository journalEntryRepository, CacheStore cache) {
        this.journalRepository = journalRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.cache = cache;
    }

    public Journal createJournal(Journal journal) {
        JournalRecord journalRecord = new JournalRecord();
        journalRecord.setJournalId(journal.getJournalId());
        journalRecord.setJournalName(journal.getJournalName());
        journalRecord.setUsername(journal.getUsername());
        journalRecord.setDateCreated(journal.getDateCreated());

        if (journal.getJournalId() == null || journal.getJournalName() == null ||
        journal.getDateCreated() == null || journal.getUsername() == null)
        {
            throw new IllegalArgumentException();
        }
        journalRepository.save(journalRecord);

        cache.add(journal.getJournalId(), Collections.EMPTY_LIST);
        return journal;
    }
    public Journal deleteJournal(Journal journal) {
        JournalRecord recordToDelete = new JournalRecord();
        recordToDelete.setJournalId(journal.getJournalId());
        recordToDelete.setJournalName(journal.getJournalName());
        recordToDelete.setDateCreated(journal.getDateCreated());
        recordToDelete.setUsername(journal.getUsername());

        JournalKey key = new JournalKey(journal.getUsername(), journal.getJournalId());

        Journal journalFound = getJournalById(journal.getJournalId());

        if (journalFound != null) {
            journalRepository.delete(recordToDelete);
            cache.evict(journal.getJournalId());
        } else {
            throw new IllegalArgumentException();
        }
        return journal;
    }
    public Journal getJournalById(String journalId) {
        Journal journalToGet = null;
        Iterable<JournalRecord> journalIterator = journalRepository.findAll();
        if (journalIterator == null) {
            journalIterator = new ArrayList<>();
        }
        for (JournalRecord record : journalIterator) {
            if (record.getJournalId().equals(journalId)) {
                journalToGet = new Journal(record.getUsername(), record.getJournalId(), record.getJournalName(), record.getDateCreated());
            }
        }
        return journalToGet;
    }

    public List<Journal> getAllJournalsForUser(String username) {
        Iterable<JournalRecord> journalIterable = journalRepository.findAll();
        List<Journal> journals = new ArrayList<>();
        for(JournalRecord j : journalIterable) {
            if (j.getUsername().equals(username)) {
                journals.add(new Journal(j.getUsername(),
                        j.getJournalId(),
                        j.getJournalName(),
                        j.getDateCreated()));
            }
        }
        cacheEntriesForJournal(journals);
        return journals;
    }

    public JournalEntry createJournalEntry(JournalEntry journalEntry) {
        JournalEntryRecord record = new JournalEntryRecord();
        record.setJournalId(journalEntry.getJournalId());
        record.setEntryId(journalEntry.getEntryId());
        record.setBody(journalEntry.getBody());
        record.setTimestamp(journalEntry.getTimestamp());
        record.setTitle(journalEntry.getTitle());

        journalEntryRepository.save(record);

        List<JournalEntry> currentlyCached = cache.get(journalEntry.getJournalId());

        if (currentlyCached == null) {
            currentlyCached = new ArrayList<JournalEntry>();
        }
        cache.evict(journalEntry.getJournalId());
        currentlyCached.add(journalEntry);
        cache.add(journalEntry.getJournalId(), currentlyCached);
        return journalEntry;
    }

    public List<JournalEntry> getEntriesForJournal(String journalId) {
        List<JournalEntry> entriesFromCache = cache.get(journalId);
        if (entriesFromCache != null && !entriesFromCache.isEmpty()) {
            return entriesFromCache;
        }
        Iterable<JournalEntryRecord> journalEntryIterable = journalEntryRepository.findAll();
        if (journalEntryIterable == null) {
            journalEntryIterable = new ArrayList<>();
        }

        List<JournalEntry> journalEntries = new ArrayList<>();

        for (JournalEntryRecord jer : journalEntryIterable) {
            if (jer.getJournalId().equals(journalId)) {
                journalEntries.add(new JournalEntry(jer.getJournalId(),
                        jer.getEntryId(),
                        jer.getTimestamp(),
                        jer.getUsername(),
                        jer.getTitle(),
                        jer.getBody()));
            }
        }

        cache.add(journalId, journalEntries);

        return journalEntries;
    }

    public void cacheEntriesForJournal(List<Journal> journals) {
        for (Journal j : journals) {
            getEntriesForJournal(j.getJournalId());
        }
    }

    public void deleteJournalEntry(String journalDeleteKey) {
        JournalEntryKey journalEntryKey = new JournalEntryKey();
        String[] keys = journalDeleteKey.split("--");
        System.out.println("EntryId: " + keys[1] + "\nJournalId: " + keys[0]);
        journalEntryKey.setJournalId(keys[0]);
        journalEntryKey.setEntryId(keys[1]);
        journalEntryRepository.deleteById(journalEntryKey);
        cache.evict(keys[0]);
    }
}
