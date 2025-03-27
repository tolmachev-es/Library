package org.tolmachev.library.service;

import org.tolmachev.library.entity.LibrarySubscriptionEntity;

import java.util.List;

public interface SenderService {

    void send(List<LibrarySubscriptionEntity> subscriptions);
}
