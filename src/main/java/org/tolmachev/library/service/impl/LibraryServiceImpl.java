package org.tolmachev.library.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tolmachev.library.entity.BookEntity;
import org.tolmachev.library.entity.LibrarySubscriptionEntity;
import org.tolmachev.library.model.Data;
import org.tolmachev.library.model.UploadRequest;
import org.tolmachev.library.repository.LibrarySubscriptionEntityRepository;
import org.tolmachev.library.service.DataStorage;
import org.tolmachev.library.service.LibraryService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class LibraryServiceImpl implements LibraryService {
    private final DataStorage dataStorage;
    private final LibrarySubscriptionEntityRepository libraryRepository;
    private final ObjectMapper objectMapper;


    @Override
    public void getSubscription(String lastName) {

    }

    @Override
    public void saveOldData(String data) {
        dataStorage.putBatchData(data);
    }

    @Override
    public void processData() throws JsonProcessingException {
        String s = dataStorage.getBatchData();
        if (s == null) {
            return;
        }
        UploadRequest uploadRequest = objectMapper.readValue(s, UploadRequest.class);
        Map<String, LibrarySubscriptionEntity> subscriptionEntities = new HashMap<>();

        List<Data> list1 = uploadRequest.getData();

        for (Data d : list1) {
            BookEntity bookEntity = new BookEntity();
            bookEntity.setName(d.getBookName());
            bookEntity.setAuthor(d.getBookAuthor());

            LibrarySubscriptionEntity subscriptionEntity;
            if (subscriptionEntities.containsKey(d.getUserFullName())) {
                subscriptionEntity = subscriptionEntities.get(d.getUserFullName());
            } else {
                subscriptionEntity = new LibrarySubscriptionEntity();
                subscriptionEntity.setFullName(d.getUserFullName());
                subscriptionEntity.setActive(d.getUserActive());
                subscriptionEntities.put(d.getUserFullName(), subscriptionEntity);
            }
            subscriptionEntity.addBook(bookEntity);
        }

        List<LibrarySubscriptionEntity> existSubscription = libraryRepository.getLibrarySubscriptionEntitiesByFullNameIn(
                subscriptionEntities.keySet());

        for (LibrarySubscriptionEntity subscriptionEntity : existSubscription) {
            LibrarySubscriptionEntity subscription = subscriptionEntities.get(subscriptionEntity.getFullName());
            if (subscription != null) {
                subscription.setId(subscriptionEntity.getId());
                subscription.setActive(subscriptionEntity.getActive());
                subscription.addAllBooks(subscriptionEntity.getAllBooks());
                subscriptionEntities.put(subscriptionEntity.getFullName(), subscription);
            }
        }

        libraryRepository.saveAll(subscriptionEntities.values());

    }

}
