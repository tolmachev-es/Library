package org.tolmachev.library.repository;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tolmachev.library.entity.BookEntity;

import java.util.Optional;

@Repository
public interface BookEntityRepository extends JpaRepository<BookEntity, Integer> {
    @Cacheable(cacheNames = "books")
    Optional<BookEntity> findBookEntityByName(String name);
}