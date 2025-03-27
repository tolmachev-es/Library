package org.tolmachev.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tolmachev.library.entity.BookEntity;

public interface BookEntityRepository extends JpaRepository<BookEntity, Integer> {
}