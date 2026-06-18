package com.rubal.caching.model;

public record IndexedFieldsInfo(String path, String alias, boolean sortable, double weight) {
}
