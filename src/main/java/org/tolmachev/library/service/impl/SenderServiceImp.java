package org.tolmachev.library.service.impl;

import org.springframework.stereotype.Service;
import org.tolmachev.library.entity.LibrarySubscriptionEntity;
import org.tolmachev.library.service.SenderService;

import java.util.List;

@Service
public class SenderServiceImp implements SenderService {

    @Override
    public void send(List<LibrarySubscriptionEntity> subscriptions) {

    }
}
