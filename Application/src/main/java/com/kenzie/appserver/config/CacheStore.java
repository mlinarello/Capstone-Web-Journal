package com.kenzie.appserver.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.kenzie.appserver.service.model.JournalEntry;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class CacheStore {
    private Cache<String, List<JournalEntry>> cache;

    public CacheStore(int expiry, TimeUnit timeUnit) {
        // initalize the cache
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expiry, timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }

    public List<JournalEntry> get(String journalId) {
        return cache.getIfPresent(journalId);
    }

    public void evict(String key) {
        if (key != null) {
            cache.invalidate(key);
        }
    }

    public void add(String key, List<JournalEntry> value) {
        cache.put(key, value);
    }
}
