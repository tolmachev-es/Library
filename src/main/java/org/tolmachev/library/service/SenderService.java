package org.tolmachev.library.service;

import org.tolmachev.library.entity.LibrarySubscriptionEntity;

import java.util.Set;

public interface SenderService {

    void send(Set<LibrarySubscriptionEntity> subscriptionEntities);
}
