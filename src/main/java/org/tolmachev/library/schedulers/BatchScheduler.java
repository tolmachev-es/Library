package org.tolmachev.library.schedulers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tolmachev.library.service.DataStorage;

@Component
@Slf4j
public class BatchScheduler {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job redisToDbJob;
    @Autowired
    private DataStorage dataStorage;

    @Scheduled(fixedDelay = 15000)
    public void runBatchJob() {
        try {
            log.info("Batch Job started");
            if (dataStorage.hasValue()) {
                log.info("Redis DB Job is running");
                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();
                jobLauncher.run(redisToDbJob, jobParameters);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
