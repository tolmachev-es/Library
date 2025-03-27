package org.tolmachev.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tolmachev.library.entity.DataEntity;

import java.util.Optional;

@Repository
public interface DataEntityRepository extends JpaRepository<DataEntity, Integer> {
    Optional<DataEntity> findFirstByOrderByIdDesc();
}