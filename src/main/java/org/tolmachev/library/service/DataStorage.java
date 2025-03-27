package org.tolmachev.library.service;

public interface DataStorage {
    String getBatchData();
    void putBatchData(String value);
    boolean hasValue();
}
