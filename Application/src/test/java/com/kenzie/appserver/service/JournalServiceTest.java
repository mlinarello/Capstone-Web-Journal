package com.kenzie.appserver.service;
import com.kenzie.appserver.config.CacheStore;
import com.kenzie.appserver.repositories.JournalEntryRepository;
import com.kenzie.appserver.repositories.JournalRepository;
import com.kenzie.appserver.repositories.model.JournalKey;
import com.kenzie.appserver.repositories.model.JournalRecord;
import com.kenzie.appserver.service.model.Journal;
import com.kenzie.appserver.service.model.JournalEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

public class JournalServiceTest {
    private JournalRepository journalRepository;

    private JournalEntryRepository journalEntryRepository;

    private CacheStore cacheStore;

    private JournalService journalService;

    @BeforeEach
    public void setup() {
        journalRepository = mock(JournalRepository.class);
        journalEntryRepository = mock(JournalEntryRepository.class);
        cacheStore = mock(CacheStore.class);
        journalService = new JournalService(journalRepository, journalEntryRepository, cacheStore);
    }

    /** ------------------------------------------------------------------------
     *  JournalService.createJournal
     *  ------------------------------------------------------------------------ **/

    @Test
    void createJournal_goodJournal_journalCreated() {
        String username = "username";
        String journalId = UUID.randomUUID().toString();
        String journalName = "name";
        String dateCreated = "date";

        Journal journal = new Journal(username, journalId, journalName, dateCreated);

        JournalRecord record = new JournalRecord();
        record.setJournalId(journalId);
       record.setJournalName(journalName);
       record.setUsername(username);
       record.setDateCreated(dateCreated);

       journalService.createJournal(journal);

        ArgumentCaptor<JournalRecord> recordCaptor = ArgumentCaptor.forClass(JournalRecord.class);
        verify(journalRepository).save(recordCaptor.capture());

        verify(cacheStore).add(any(), any());
        JournalRecord createdRecord = recordCaptor.getValue();

        Assertions.assertNotNull(createdRecord, "The journal record is returned");
        Assertions.assertEquals(createdRecord.getJournalId(), record.getJournalId(), "The journal id matches");
        Assertions.assertEquals(createdRecord.getJournalName(), record.getJournalName(), "The journal name matches");
        Assertions.assertEquals(createdRecord.getDateCreated(), record.getDateCreated(), "The journal date matches");
        Assertions.assertEquals(createdRecord.getUsername(), record.getUsername(), "The concert ticket price matches");
    }

    @Test
    void createJournal_JournalMissingFields_throwIllegalArgumentExceptionAndNoRepoSaveCall() {
        String username = "username";
        String journalId = UUID.randomUUID().toString();
        String journalName = "name";
        String dateCreated = "date";

        Journal journalMissingOneField = new Journal(username, journalId, null, dateCreated);
        Journal journalMissingTwoFields = new Journal(null, journalId, null, dateCreated);
        Journal journalWithAlmostNoFields = new Journal(null, journalId, null, null);

        List<Journal> journals = new ArrayList<Journal>();
        journals.add(journalMissingOneField);
        journals.add(journalMissingTwoFields);
        journals.add(journalWithAlmostNoFields);

        for (Journal journal : journals) {
            Assertions.assertThrows(IllegalArgumentException.class,
                    () -> journalService.createJournal(journal));
            verifyZeroInteractions(journalRepository);
        }
    }

    /** ------------------------------------------------------------------------
     *  JournalService.deleteJournal
     *  ------------------------------------------------------------------------ **/

    @Test
    void deleteJournal_journalExists_callsRepoDeleteAndCacheEvict() {
        String username = "username";
        String journalId = UUID.randomUUID().toString();
        String journalName = "name";
        String dateCreated = "date";

        Journal journal = new Journal(username, journalId, journalName, dateCreated);

        JournalKey key = new JournalKey(username, journalId);

        JournalRecord recordToDelete = new JournalRecord(key, journalName, dateCreated);

        when(journalRepository.existsById(any())).thenReturn(true);

        when(journalRepository.findAll()).thenReturn(Collections.singletonList(recordToDelete));

        journalService.deleteJournal(journal);

        verify(journalRepository).delete(any());

        verify(cacheStore).evict(eq(journalId));
    }

    @Test
    void deleteJournal_journalDoesNotExist_noDeleteOrEvictCall() {
        String username = "username";
        String journalId = UUID.randomUUID().toString();
        String journalName = "name";
        String dateCreated = "date";

        Journal journal = new Journal(username, journalId, journalName, dateCreated);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> journalService.deleteJournal(journal));
        verify(journalRepository, times(0)).delete(any());

        verify(cacheStore, times(0)).evict(any());
    }

    /** ------------------------------------------------------------------------
     *  JournalService.getJournalById
     *  ------------------------------------------------------------------------ **/

    @Test
    void getJournalById_getsJournalById() {
        String username = "username";
        String journalId = UUID.randomUUID().toString();
        String journalName = "name";
        String dateCreated = "date";

        Journal journal = new Journal(username, journalId, journalName, dateCreated);

        JournalKey key = new JournalKey(username, journalId);

        JournalRecord recordToReturn = new JournalRecord(key, journalName, dateCreated);

        ArrayList<JournalRecord> recordsReturned = new ArrayList<>();
        recordsReturned.add(recordToReturn);

        when(journalRepository.findAll()).thenReturn(recordsReturned);

        Journal retrieved = journalService.getJournalById(journalId);

        Assertions.assertEquals(retrieved.getUsername(), username);
        Assertions.assertEquals(retrieved.getJournalId(), journalId);
        Assertions.assertEquals(retrieved.getDateCreated(), dateCreated);
        Assertions.assertEquals(retrieved.getJournalName(), journalName);
    }

    /** ------------------------------------------------------------------------
     *  JournalService.updateJournal
     *  ------------------------------------------------------------------------ **/

    //DEPRECATED


    /** ------------------------------------------------------------------------
     *  JournalService.getAllJournalForUser
     *  ------------------------------------------------------------------------ **/

    @Test
    void getAllJournals_goodUserWithJournals_getsAllAndOnlyJournalsForUser() {
        String username = "username";
        String journalId = UUID.randomUUID().toString();
        String journalName = "journalName";
        String dateCreated = "dateCreated";

        Journal journal = new Journal(username, journalId, journalName , dateCreated);

        JournalRecord record = new JournalRecord();
        record.setJournalId(journalId);
        record.setJournalName(journalName);
        record.setUsername(username);
        record.setDateCreated(dateCreated);

        String journalId1 = UUID.randomUUID().toString();
        String journalName1 = "journalname1";
        String dateCreated1 = "dateCreated1";

        Journal journal1 = new Journal(username, journalId1 , journalName1 , dateCreated1);

        JournalRecord record1 = new JournalRecord();
        record1.setUsername(username);
        record1.setJournalId(journalId1);
        record1.setJournalName(journalName1);
        record1.setDateCreated(dateCreated1);

        String username2 = "username2";
        String journalId2 = UUID.randomUUID().toString();
        String journalName2 = "journalName2";
        String dateCreated2 = "dateCreated2";

        Journal journal2 = new Journal(username2, journalId2 , journalName2 , dateCreated2);

        JournalRecord record2 = new JournalRecord();
        record2.setUsername(username2);
        record2.setJournalId(journalId2);
        record2.setJournalName(journalName2);
        record2.setDateCreated(dateCreated2);

        List<JournalRecord> recordsList = new ArrayList<>();
        recordsList.add(record);
        recordsList.add(record1);
        recordsList.add(record2);

        when(journalRepository.findAll()).thenReturn(recordsList);

        List<Journal> expected = new ArrayList<>();
        expected.add(journal);
        expected.add(journal1);

        List<Journal> response = journalService.getAllJournalsForUser(username);

        verify(journalRepository).findAll();

        List<String> expectedJournalIds = new ArrayList<>();
        for(Journal exJournal : expected) {
            expectedJournalIds.add(exJournal.getJournalId());
        }

        List<String> returnedJournalIds = new ArrayList<>();
        for (Journal retJournal : response) {
            returnedJournalIds.add(retJournal.getJournalId());
        }

        Assertions.assertEquals(returnedJournalIds, expectedJournalIds);
    }

    /** ------------------------------------------------------------------------
     *  JournalService.createJournalEntry
     *  ------------------------------------------------------------------------ **/

    @Test
    public void createJournalEntry_goodInfo_entryCreated() {
        String journalId = UUID.randomUUID().toString();
        String journalEntryId = UUID.randomUUID().toString();
        String timestamp = LocalDateTime.now().toString();
        String username = "fantasma";
        String title = "Such a Sweet Title";
        String body = "A journal body for the ages.";

        JournalEntry entry = new JournalEntry(journalId, journalEntryId, timestamp, username, title, body);

        journalService.createJournalEntry(entry);

        verify(journalEntryRepository).save(any());

        verify(cacheStore).get(journalId);

        verify(cacheStore).evict(journalId);

        verify(cacheStore).add(any(), any());
    }

    /** ------------------------------------------------------------------------
     *  JournalService.getEntriesForJournal
     *  ------------------------------------------------------------------------ **/

}