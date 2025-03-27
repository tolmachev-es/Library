package org.tolmachev.library.controller;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.tolmachev.library.model.Subscription;
import org.tolmachev.library.service.LibraryService;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(path = "/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.OK)
    void upload(InputStream inputStream){
        String body = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        libraryService.saveOldData(body);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    Subscription search(@RequestParam(name = "fio") String fio) {
        return libraryService.getSubscription(fio);
    }
}
