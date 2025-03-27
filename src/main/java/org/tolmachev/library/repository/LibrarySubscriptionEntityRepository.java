package org.tolmachev.library.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.tolmachev.library.entity.LibrarySubscriptionEntity;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LibrarySubscriptionEntityRepository extends JpaRepository<LibrarySubscriptionEntity, Integer> {
    @Query(value = "SELECT DISTINCT s.* FROM SUBSCRIPTION s "
            + "JOIN SUBSCRIPTION_BOOK sb on s.id = sb.book_id "
            + "WHERE sb.start_date < :startDate "
            + "AND sb.start_date IS NOT NULL", nativeQuery = true)
    Set<LibrarySubscriptionEntity> findSubscription(@Param("startDate")LocalDate startDate);

    @Cacheable(cacheNames = "usernames")
    Optional<LibrarySubscriptionEntity> findLibrarySubscriptionEntityByUsername(String username);

    Optional<LibrarySubscriptionEntity> findLibrarySubscriptionEntityByFullNameIgnoreCase(String fullName);
}