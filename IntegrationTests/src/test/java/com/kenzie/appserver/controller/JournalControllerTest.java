package com.kenzie.appserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kenzie.appserver.IntegrationTest;
import com.kenzie.appserver.controller.model.CreateEntryRequest;
import com.kenzie.appserver.controller.model.CreateJournalRequest;
import com.kenzie.appserver.service.JournalService;
import com.kenzie.appserver.service.model.Journal;
import com.kenzie.appserver.service.model.JournalEntry;
import net.andreinc.mockneat.MockNeat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
public class JournalControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    JournalService journalService;

    private final MockNeat mockNeat = MockNeat.threadLocal();

    private final ObjectMapper mapper = new ObjectMapper();

    /** ------------------------------------------------------------------------
     *  createJournal
     *  ------------------------------------------------------------------------ **/

    @Test
    public void createJournal_goodJournal_CreatesJournal() throws Exception {
        //GIVEN
        String username = mockNeat.strings().valStr();
        String journalName = mockNeat.strings().valStr();

        CreateJournalRequest createJournalRequest = new CreateJournalRequest();
        createJournalRequest.setUsername(username);
        createJournalRequest.setJournalName(journalName);

        //WHEN
        mvc.perform(post("/journal/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createJournalRequest)))

                //THEN
                .andExpect(status().isOk())
                .andExpect(jsonPath("username")
                        .value(is(username)))
                .andExpect(jsonPath("journalId")
                        .exists())
                .andExpect(jsonPath("journalName")
                        .value(is(journalName)))
                .andExpect(jsonPath("dateCreated")
                        .exists());
    }

    @Test
    public void createJournal_badJournal_returnsBadRequest() throws Exception {
        //no username
        CreateJournalRequest createJournalRequest = new CreateJournalRequest();
        createJournalRequest.setJournalName("aname");

        //WHEN
        mvc.perform(post("/journal/create")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createJournalRequest)))

                //THEN
                .andExpect(status().isBadRequest());
    }

    /** ------------------------------------------------------------------------
     *  deleteJournal
     *  ------------------------------------------------------------------------ **/

    @Test
    public void deleteJournal_journalExists_journalIsDeleted() throws Exception {
        String username = "username";
        String journalId = UUID.randomUUID().toString();
        String journalName = "name";
        String dateCreated = "date";

        Journal journal = new Journal(username, journalId, journalName, dateCreated);

        Journal persistedJournal = journalService.createJournal(journal);

        mvc.perform(delete("/journal/{journalId}", persistedJournal.getJournalId())
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk());
    }

    @Test
    public void deleteJournal_journalDoesntExist_badRequest() throws Exception {
        String journalId = UUID.randomUUID().toString();

        mvc.perform(delete("/journal/{journalId}", journalId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }
    /** ------------------------------------------------------------------------
     *  getJournalById
     *  ------------------------------------------------------------------------ **/

    @Test
    public void getJournalById_journalExists_getsJournal() throws Exception {
        String username = "username";
        String journalId = UUID.randomUUID().toString();
        String journalName = "name";
        String dateCreated = "date";

        Journal journal = new Journal(username, journalId, journalName, dateCreated);

        Journal persistedJournal = journalService.createJournal(journal);

        mvc.perform(get("/journal/{journalId}", journalId)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("username")
                        .value(is(username)))
                .andExpect(jsonPath("journalId")
                        .value(is(journalId)))
                .andExpect(jsonPath("dateCreated")
                        .value(is(dateCreated)))
                .andExpect(jsonPath("journalName")
                        .value(is(journalName)))
                .andExpect(status().isOk());
    }

    @Test
    public void getJournalById_journalDoesNotExist_notFound() throws Exception {
        String journalId = UUID.randomUUID().toString();

        mvc.perform(get("/journal/{journalId}", journalId)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }
    /** ------------------------------------------------------------------------
     *  getAllJournalsForUser
     *  ------------------------------------------------------------------------ **/

    @Test
    public void getAllJournalsForUser_withTwoJournals_getsTwoJournals() throws Exception {
        String username = mockNeat.names().toString();

        String journalName1 = mockNeat.strings().toString();

        String journalName2 = mockNeat.strings().toString();

        CreateJournalRequest request1 = new CreateJournalRequest();
        request1.setJournalName(journalName1);
        request1.setUsername(username);

        CreateJournalRequest request2 = new CreateJournalRequest();
        request2.setUsername(username);
        request2.setJournalName(journalName2);

        mvc.perform(post("/journal/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request1)));

        mvc.perform(post("/journal/create")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request2)));

        mvc.perform(get("/journal/all/{username}", username)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is(username)))
                .andExpect(jsonPath("$[1].username", is(username)));

    }

    @Test
    public void getAllJournalsForUser_withNoJournals_noJournalsReturned() throws Exception {
        mvc.perform(get("/journal/all/{username}", mockNeat.names().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    /** ------------------------------------------------------------------------
     *  createJournalEntry
     *  ------------------------------------------------------------------------ **/

    @Test
    public void createJournalEntry_goodEntry_createsEntry() throws Exception {
        String journalId = mockNeat.uuids().toString();
        String username = mockNeat.names().toString();
        String title = mockNeat.words().toString();
        String body = mockNeat.strings().toString();

        CreateEntryRequest entryRequest = new CreateEntryRequest();
        entryRequest.setJournalId(journalId);
        entryRequest.setUsername(username);
        entryRequest.setBody(body);
        entryRequest.setTitle(title);

        mvc.perform(post("/journal/entry")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(entryRequest)))

                .andExpect(jsonPath("journalEntryId")
                        .exists())
                .andExpect(jsonPath("username")
                        .value(is(username)))
                .andExpect(jsonPath("timestamp")
                        .exists())
                .andExpect(jsonPath("journalId")
                        .value(is(journalId)))
                .andExpect(jsonPath("title")
                        .value(is(title)))
                .andExpect(jsonPath("body")
                        .value(is(body)))
                .andExpect(status().isOk());
    }

    @Test
    public void createJournalEntry_missingFields_badRequest() throws Exception {
        String journalId = mockNeat.uuids().toString();
        String username = mockNeat.names().toString();
        String title = mockNeat.words().toString();
        String body = mockNeat.strings().toString();

        CreateEntryRequest entryRequest = new CreateEntryRequest();
        entryRequest.setUsername(username);
        entryRequest.setBody(body);

        mvc.perform(post("/journal/entry")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(entryRequest)))

                .andExpect(status().isBadRequest());
    }
    /** ------------------------------------------------------------------------
     *  getAllEntryForJournal
     *  ------------------------------------------------------------------------ **/

    @Test
    public void getAllEntryForJournal_entriesExist_returnsOnlyEntriesForJournal() throws Exception {
        String goodJournalId = mockNeat.uuids().toString();
        String journalEntryId = mockNeat.uuids().toString();
        String timestamp = LocalDateTime.now().toString();
        String username = mockNeat.names().toString();
        String title = mockNeat.words().toString();
        String body = mockNeat.strings().toString();

        String journalEntryId2 = mockNeat.uuids().toString();
        String timestamp2 = LocalDateTime.now().toString();
        String username2 = mockNeat.names().toString();
        String title2 = mockNeat.words().toString();
        String body2 = mockNeat.strings().toString();

        String badJournalId = "notTheJournalYouWant";
        String journalEntryId3 = mockNeat.uuids().toString();
        String timestamp3 = LocalDateTime.now().toString();
        String username3 = mockNeat.names().toString();
        String title3 = mockNeat.words().toString();
        String body3 = mockNeat.strings().toString();

        JournalEntry entry1 = new JournalEntry(goodJournalId, journalEntryId, timestamp, username, title, body);
        JournalEntry entry2 = new JournalEntry(goodJournalId, journalEntryId2, timestamp2, username2, title2, body2);
        JournalEntry entry3 = new JournalEntry(badJournalId, journalEntryId3, timestamp3, username3, title3, body3);

        journalService.createJournalEntry(entry1);
        journalService.createJournalEntry(entry2);
        journalService.createJournalEntry(entry3);

        mvc.perform(get("/journal/entry/all/{journalId}", goodJournalId)
                .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].journalId", is(goodJournalId)))
                .andExpect(jsonPath("$[1].journalId", is(goodJournalId)));
    }

    @Test
    public void getAllEntryForJournal_badJournal_noEntries() throws Exception {
        String goodJournalId = mockNeat.uuids().toString();
        String journalEntryId = mockNeat.uuids().toString();
        String timestamp = LocalDateTime.now().toString();
        String username = mockNeat.names().toString();
        String title = mockNeat.words().toString();
        String body = mockNeat.strings().toString();

        String journalEntryId2 = mockNeat.uuids().toString();
        String timestamp2 = LocalDateTime.now().toString();
        String username2 = mockNeat.names().toString();
        String title2 = mockNeat.words().toString();
        String body2 = mockNeat.strings().toString();

        String badJournalId = "notTheJournalYouWant";
        String journalEntryId3 = mockNeat.uuids().toString();
        String timestamp3 = LocalDateTime.now().toString();
        String username3 = mockNeat.names().toString();
        String title3 = mockNeat.words().toString();
        String body3 = mockNeat.strings().toString();

        JournalEntry entry1 = new JournalEntry(goodJournalId, journalEntryId, timestamp, username, title, body);
        JournalEntry entry2 = new JournalEntry(goodJournalId, journalEntryId2, timestamp2, username2, title2, body2);
        JournalEntry entry3 = new JournalEntry(badJournalId, journalEntryId3, timestamp3, username3, title3, body3);

        journalService.createJournalEntry(entry1);
        journalService.createJournalEntry(entry2);
        journalService.createJournalEntry(entry3);

        mvc.perform(get("/journal/entry/all/{journalId}", "jdkdjcbdkjb")
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
