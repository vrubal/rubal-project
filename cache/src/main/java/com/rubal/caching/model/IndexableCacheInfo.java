package com.rubal.caching.model;

import com.rubal.caching.annotations.IndexCreationMode;

import java.util.List;

public record IndexableCacheInfo(String name, String indexAlias, String keyPrefix, IndexCreationMode creationMode, List<IndexedFieldsInfo> indexableProperties) {
}