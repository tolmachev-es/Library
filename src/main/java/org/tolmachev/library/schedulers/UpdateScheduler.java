package org.tolmachev.library.schedulers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tolmachev.library.service.DataStorage;
import org.tolmachev.library.service.impl.LibraryUpdateService;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateScheduler {
    @Qualifier("postgreService")
    private final DataStorage mainStorage;
    @Qualifier("redisService")
    private final DataStorage storage;
    private final LibraryUpdateService libraryUpdateService;

    @Scheduled(fixedDelay = 15000)
    public void runBatchJob() {
        log.info("Starting batch job");
        String batchData = storage.getBatchData();
        while (batchData != null) {
            try {
                libraryUpdateService.updateDatabase(batchData);
                log.info("Batch job completed");
                batchData = storage.getBatchData();
            } catch (Exception e) {
                mainStorage.putBatchData(batchData);
                log.error("Batch job failed", e);
            }
        }
    }
}
