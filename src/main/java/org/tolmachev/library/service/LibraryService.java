package org.tolmachev.library.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface LibraryService {
    void getSubscription(String lastName);
    void saveOldData(String data);
    void processData() throws JsonProcessingException;
}
