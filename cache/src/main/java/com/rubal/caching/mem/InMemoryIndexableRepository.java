package com.rubal.caching.mem;

import com.rubal.caching.indexing.IndexableRepository;
import com.rubal.caching.model.IndexableCacheInfo;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

public class InMemoryIndexableRepository implements IndexableRepository<Object, Object, Object> {

    @Override
    public boolean isIndexable() {
        return false;
    }

    @Override
    public Object put(Object key, Object value) {
        // do nothing
        return null;
    }

    @Override
    public void delete(Object key) {
        // do nothing
    }

    @Override
    public Page<Object> search(String query, Pageable pageable) {
        throw new UnsupportedOperationException("Search not supported for in memory caching");
    }

    @Override
    public IndexableCacheInfo getIndexableCacheInfo() {
        throw new UnsupportedOperationException("Indexing not supported for in memory caching");
    }
}
