package org.tolmachev.library.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.tolmachev.library.entity.BookEntity;
import org.tolmachev.library.entity.BookInSubscriptionEntity;
import org.tolmachev.library.entity.LibrarySubscriptionEntity;
import org.tolmachev.library.mappers.SubscriptionMapper;
import org.tolmachev.library.model.Book;
import org.tolmachev.library.model.Data;
import org.tolmachev.library.model.Subscription;
import org.tolmachev.library.model.UploadRequest;
import org.tolmachev.library.repository.LibrarySubscriptionEntityRepository;
import org.tolmachev.library.service.impl.LibraryServiceImpl;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.tolmachev.library.BaseTestClass.*;

@SpringBootTest
@Transactional
class LibraryIntegrationTest {
    @Autowired
    private LibraryServiceImpl libraryService;
    @Autowired
    private LibrarySubscriptionEntityRepository repository;
    @Autowired
    private SubscriptionMapper subscriptionMapper;

    @Test
    void checkCountSave() {
        UploadRequest uploadRequest = new UploadRequest(List.of(getDataFirst(), getDataSecond(), getDataThird()));
        libraryService.saveOldData(uploadRequest);
        List<LibrarySubscriptionEntity> all = repository.findAll();

        assertEquals(3, all.size());
        Set<BookInSubscriptionEntity> collect = all.stream()
                                                   .map(LibrarySubscriptionEntity::getBooks)
                                                   .flatMap(Collection::stream)
                                                   .collect(Collectors.toSet());
        assertEquals(3, collect.size());
    }

    @Test
    void checkSaveOldData(){
        Data dataFirst = getDataFirst();
        UploadRequest uploadRequest = new UploadRequest(List.of(getDataFirst()));
        libraryService.saveOldData(uploadRequest);
        List<LibrarySubscriptionEntity> all = repository.findAll();

        assertEquals(1, all.size());


        LibrarySubscriptionEntity subscription = all.get(0);

        assertEquals(dataFirst.getUsername(), subscription.getUsername());
        assertEquals(dataFirst.getUserFullName(), subscription.getFullName());
        assertEquals(dataFirst.getUserActive(), subscription.getActive());

        Set<BookInSubscriptionEntity> books = subscription.getBooks();
        assertEquals(1, books.size());

        BookEntity book = books.iterator().next().getBook();

        assertEquals(dataFirst.getBookAuthor(), book.getAuthor());
        assertEquals(dataFirst.getBookName(), book.getName());
    }

    @Test
    void checkResponse() {
        Data dataFirst = getDataFirst();
        UploadRequest uploadRequest = new UploadRequest(List.of(getDataFirst()));
        libraryService.saveOldData(uploadRequest);
        List<LibrarySubscriptionEntity> all = repository.findAll();

        assertEquals(1, all.size());


        LibrarySubscriptionEntity subscription = all.get(0);
        Subscription response = subscriptionMapper.map(subscription);

        assertEquals(dataFirst.getUsername(), response.getUsername());
        assertEquals(dataFirst.getUserFullName(), response.getFullName());
        assertEquals(dataFirst.getUserActive(), response.getActive());

        assertEquals(1, response.getBooks().size());

        Book book = response.getBooks().iterator().next();

        assertEquals(dataFirst.getBookName(), book.getBookName());
        assertEquals(dataFirst.getBookAuthor(), book.getBookAuthor());
    }

    @Test
    void checkFindUser() {
        Data dataFirst = getDataFirst();
        UploadRequest uploadRequest = new UploadRequest(List.of(getDataFirst()));
        libraryService.saveOldData(uploadRequest);

        Subscription subscription = libraryService.getSubscription(dataFirst.getUserFullName());

        assertEquals(dataFirst.getUsername(), subscription.getUsername());
        assertEquals(dataFirst.getUserFullName(), subscription.getFullName());
        assertEquals(dataFirst.getUserActive(), subscription.getActive());

        assertEquals(1, subscription.getBooks().size());

        Book book = subscription.getBooks().iterator().next();

        assertEquals(dataFirst.getBookName(), book.getBookName());
        assertEquals(dataFirst.getBookAuthor(), book.getBookAuthor());
    }

}
