package org.tolmachev.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tolmachev.library.entity.BookInSubscriptionEntity;
import org.tolmachev.library.entity.BookSubscriptionId;

public interface BookInSubscriptionEntityRepository extends JpaRepository<BookInSubscriptionEntity, BookSubscriptionId> {
}