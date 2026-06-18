package com.rubal.caching.indexing;

import com.rubal.caching.model.IndexableCacheInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IndexableRepository<D, K, V> {

    public boolean isIndexable();

    public D put(K key, V value);

    public void delete(K key);

    public Page<D> search(String query, Pageable pageable);

    public IndexableCacheInfo getIndexableCacheInfo();

}
