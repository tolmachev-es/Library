package org.tolmachev.library.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tolmachev.library.advice.ExceptionControllerAdvice;
import org.tolmachev.library.exceptions.SubscriptionNotFoundException;
import org.tolmachev.library.model.ErrorMessage;
import org.tolmachev.library.model.UploadRequest;
import org.tolmachev.library.service.LibraryService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class LibraryControllerTest {
    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private LibraryController libraryController;

    @InjectMocks
    private ExceptionControllerAdvice exceptionControllerAdvice;

    @Test
    void upload() {
        UploadRequest uploadRequest = new UploadRequest(List.of());
        assertDoesNotThrow(() ->  libraryController.upload(uploadRequest));
        verify(libraryService, times(1)).saveOldData(uploadRequest);
    }

    @Test
    void handleSubscriptionFound() {
        String userFullName = "test user";
        assertDoesNotThrow(() ->  libraryController.search(userFullName));
        verify(libraryService, times(1)).getSubscription(userFullName);
    }

    @Test
    void handleSubscriptionNotFound(){
        SubscriptionNotFoundException exception = new SubscriptionNotFoundException("Пользователь не найден");

        ErrorMessage errorMessage = exceptionControllerAdvice.subscriptionException(exception);
        assertEquals("Пользователь не найден", errorMessage.getError());
    }
}
