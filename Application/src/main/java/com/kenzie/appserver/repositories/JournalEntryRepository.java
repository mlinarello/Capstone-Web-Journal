package com.kenzie.appserver.repositories;


import com.kenzie.appserver.repositories.model.JournalEntryKey;
import com.kenzie.appserver.repositories.model.JournalEntryRecord;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface JournalEntryRepository extends CrudRepository<JournalEntryRecord, JournalEntryKey> {
}
