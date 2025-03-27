package org.tolmachev.library.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.tolmachev.library.entity.BookEntity;
import org.tolmachev.library.entity.LibrarySubscriptionEntity;
import org.tolmachev.library.model.Data;
import org.tolmachev.library.model.UploadRequest;
import org.tolmachev.library.repository.LibrarySubscriptionEntityRepository;
import org.tolmachev.library.service.impl.RedisService;

import javax.sql.DataSource;
import java.util.*;

@Configuration
@EnableBatchProcessing
@Slf4j
@RequiredArgsConstructor
public class BatchConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RedisService redisService;
    private final LibrarySubscriptionEntityRepository libraryRepository;
    private final ObjectMapper objectMapper;


    @Bean
    public ItemReader<UploadRequest> redisDataReader() throws JsonProcessingException {
        String s = redisService.getBatchData();
        if (s != null) {
            return () -> {
                log.info("reader started");
                return objectMapper.readValue(s, UploadRequest.class);
            };
        } else {
            return () -> null;
        }
    }

    @Bean
    public ItemWriter<UploadRequest> itemWriter() {
        return list -> {
            log.info("writer started");
            Map<String, LibrarySubscriptionEntity> subscriptionEntities = new HashMap<>();
            //List<Data> list1 = list.getItems().stream().flatMap(List::stream).toList();

            List<Data> list2 = list.getItems().stream()
                                   .map(UploadRequest::getData)
                                   .flatMap(List::stream)
                                   .toList();

            for (Data d : list2) {
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
        };
    }

    @Bean
    public Step step() {
        try {
            return new StepBuilder("step", jobRepository)
                    .<UploadRequest, UploadRequest>chunk(10, transactionManager)
                    .reader(redisDataReader())
                    .writer(itemWriter())
                    .transactionManager(new ResourcelessTransactionManager())
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public Job myJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("myJob", jobRepository)
                .start(step)
                .preventRestart()
                .build();
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
        DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(
                new ClassPathResource("db/schema-postgresql.sql")
        ));
        return initializer;
    }
}

