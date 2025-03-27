package org.tolmachev.library.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.tolmachev.library.entity.DataEntity;
import org.tolmachev.library.repository.DataEntityRepository;
import org.tolmachev.library.service.DataStorage;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostgreService implements DataStorage {
    private final DataEntityRepository dataEntityRepository;

    @Override
    public String getBatchData() {
        log.info("Get batch data from Postgre");
        Optional<DataEntity> firstByOrderByIdDesc = dataEntityRepository.findFirstByOrderByIdDesc();
        if (firstByOrderByIdDesc.isPresent()) {
            String batchData = firstByOrderByIdDesc.get().getData();
            dataEntityRepository.delete(firstByOrderByIdDesc.get());
            return batchData;
        }
        return null;
    }

    @Override
    public void putBatchData(String value) {
        log.info("Put batch data from Postgre");
        DataEntity dataEntity = new DataEntity();
        dataEntity.setData(value);
        dataEntityRepository.save(dataEntity);
    }

    @Override
    public boolean hasValue() {
        log.info("Check if Postgre has value");
        Optional<DataEntity> firstByOrderByIdDesc = dataEntityRepository.findFirstByOrderByIdDesc();
        if (firstByOrderByIdDesc.isPresent()) {
            log.info("Postgre has value");
            return true;
        } else {
            log.info("Postgres hasn't value");
            return false;
        }
    }
}
