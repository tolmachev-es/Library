package org.tolmachev.library.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tolmachev.library.service.LibraryService;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping(path = "/library")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @PostMapping("/upload")
    ResponseEntity<Void> upload(InputStream inputStream){
        String body = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        libraryService.saveOldData(body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/search")
    ResponseEntity<Void> search(@RequestParam(name = "lastName") String lastName) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/uploadtest")
    ResponseEntity<Void> uploadtest(){
        try {
            libraryService.processData();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
