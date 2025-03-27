package org.tolmachev.library.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tolmachev.library.entity.BookEntity;
import org.tolmachev.library.entity.LibrarySubscriptionEntity;
import org.tolmachev.library.model.Data;
import org.tolmachev.library.model.UploadRequest;
import org.tolmachev.library.repository.BookEntityRepository;
import org.tolmachev.library.repository.LibrarySubscriptionEntityRepository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
public class LibraryUpdateService {
    private final LibrarySubscriptionEntityRepository libraryRepository;

    private final ConcurrentHashMap<String, LibrarySubscriptionEntity> subscriptions = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, BookEntity> books = new ConcurrentHashMap<>();

    private final BookEntityRepository bookRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void updateDatabase(String data) throws JsonProcessingException {
        try(ExecutorService executor = Executors.newFixedThreadPool(4)) {
            UploadRequest uploadRequest = objectMapper.readValue(data, UploadRequest.class);


            List<CompletableFuture<Void>> futures = uploadRequest.getData().stream()
                                                                 .map(item -> CompletableFuture.runAsync(() -> createData(item), executor))
                                                                 .toList();
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).join();
            libraryRepository.saveAll(subscriptions.values());
            subscriptions.clear();
            books.clear();
        }
    }


    private void createData(Data data) {
        BookEntity bookEntity;
        if (books.containsKey(data.getBookName())) {
            bookEntity = books.get(data.getBookName());
        } else {
            Optional<BookEntity> bookEntityByName = bookRepository.findBookEntityByName(data.getBookName());
            if (bookEntityByName.isPresent()) {
                bookEntity = bookEntityByName.get();
            } else {
                bookEntity = new BookEntity();
                bookEntity.setName(data.getBookName());
                bookEntity.setAuthor(data.getBookAuthor());
            }
            books.put(data.getBookName(), bookEntity);
        }

        LibrarySubscriptionEntity subscription;
        if (subscriptions.containsKey(data.getUsername())) {
            subscription = subscriptions.get(data.getUsername());
        } else {
            Optional<LibrarySubscriptionEntity> existSubscription = libraryRepository.findLibrarySubscriptionEntityByUsername(data.getUsername());
            if (existSubscription.isPresent()) {
                subscription = existSubscription.get();
            } else {
                subscription = new LibrarySubscriptionEntity();
                subscription.setUsername(data.getUsername());
                subscription.setActive(data.getUserActive());
                subscription.setFullName(data.getUserFullName());
            }
            subscriptions.put(data.getUsername(), subscription);
        }
        subscription.addBook(bookEntity);
    }
}
