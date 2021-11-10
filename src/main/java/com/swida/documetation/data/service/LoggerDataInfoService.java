package com.swida.documetation.data.service;

import com.swida.documetation.data.dto.LoggerDataInfoDTO;
import com.swida.documetation.data.entity.LoggerDataInfo;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.LoggerOperationType;
import com.swida.documetation.data.enums.StorageType;

import java.util.List;

public interface LoggerDataInfoService {
    LoggerDataInfo save(LoggerDataInfo info);
    LoggerDataInfo save(BreedOfTree breedOfTree,StorageType storageType, LoggerOperationType operationType, Object before, Object after);
    LoggerDataInfo findById(int id);
    List<LoggerDataInfoDTO> findAll();
    void deleteByID(int id);
}
