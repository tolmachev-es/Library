package org.tolmachev.library.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.tolmachev.library.exceptions.SubscriptionNotFoundException;
import org.tolmachev.library.mappers.SubscriptionMapper;
import org.tolmachev.library.model.Subscription;
import org.tolmachev.library.repository.LibrarySubscriptionEntityRepository;
import org.tolmachev.library.service.DataStorage;
import org.tolmachev.library.service.LibraryService;


@Service
@AllArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    @Qualifier("redisService")
    private final DataStorage dataStorage;
    private final LibrarySubscriptionEntityRepository libraryRepository;
    private final SubscriptionMapper subscriptionMapper;


    @Override
    public Subscription getSubscription(String fio) {
        return subscriptionMapper.map(libraryRepository.findLibrarySubscriptionEntityByFullNameIgnoreCase(fio)
                                .orElseThrow(() -> new SubscriptionNotFoundException(String.format("Пользователь с именем %s не найден", fio))));

    }

    @Override
    public void saveOldData(String data) {
        dataStorage.putBatchData(data);
    }
}
