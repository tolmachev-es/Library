package org.tolmachev.library.schedulers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.tolmachev.library.entity.LibrarySubscriptionEntity;
import org.tolmachev.library.repository.LibrarySubscriptionEntityRepository;
import org.tolmachev.library.service.SenderService;

import java.time.LocalDate;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SendScheduler {
    @Value("{scheduler.days}")
    private String days;
    private final SenderService senderService;
    private final LibrarySubscriptionEntityRepository subscriptionRepository;


    @Scheduled(fixedDelay = 6000)
    public void runSenderService() {
        LocalDate now = LocalDate.now();

        Set<LibrarySubscriptionEntity> subscription = subscriptionRepository.findSubscription(now);
        for (LibrarySubscriptionEntity subscriptionEntity : subscription) {
            System.out.println(subscriptionEntity.toString());
        }
    }
}
