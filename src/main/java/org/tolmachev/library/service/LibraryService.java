package org.tolmachev.library.service;

import org.springframework.stereotype.Service;
import org.tolmachev.library.model.Subscription;
import org.tolmachev.library.model.UploadRequest;

@Service
public interface LibraryService {
    Subscription getSubscription(String lastName);
    void saveOldData(UploadRequest uploadRequest);
}
