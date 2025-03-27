package org.tolmachev.library.service;

import org.tolmachev.library.model.Subscription;

public interface LibraryService {
    Subscription getSubscription(String lastName);
    void saveOldData(String data);
}
