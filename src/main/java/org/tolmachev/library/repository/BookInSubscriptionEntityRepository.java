package org.tolmachev.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tolmachev.library.entity.BookInSubscriptionEntity;
import org.tolmachev.library.entity.BookSubscriptionId;

@Repository
public interface BookInSubscriptionEntityRepository extends JpaRepository<BookInSubscriptionEntity, BookSubscriptionId> {
}