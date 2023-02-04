package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.JournalKey;
import com.kenzie.appserver.repositories.model.JournalRecord;
import com.kenzie.appserver.service.model.Journal;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface JournalRepository extends CrudRepository<JournalRecord, JournalKey> {
}
