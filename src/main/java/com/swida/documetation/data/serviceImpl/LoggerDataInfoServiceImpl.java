package com.swida.documetation.data.serviceImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.swida.documetation.data.dto.LoggerDataInfoDTO;
import com.swida.documetation.data.entity.LoggerDataInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.LoggerOperationType;
import com.swida.documetation.data.enums.StorageType;
import com.swida.documetation.data.jpa.LoggerDataInfoJPA;
import com.swida.documetation.data.service.LoggerDataInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class LoggerDataInfoServiceImpl implements LoggerDataInfoService {
    @Autowired
    private LoggerDataInfoJPA repository;

    @Autowired
    private UserCompanyService userCompanyService;

    @Override
    public LoggerDataInfo save(LoggerDataInfo info) {
        return repository.save(info);
    }

    @Override
    public LoggerDataInfo save(BreedOfTree breedOfTree,StorageType storageType, LoggerOperationType operationType, Object before, Object after) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserCompany user = userCompanyService.findByUsername(auth.getName());
        String jsonBefore = new Gson().toJson(before);
        String jsonAfter = new Gson().toJson(after);
        LoggerDataInfo dataInfo = new LoggerDataInfo(breedOfTree,user,operationType,storageType,jsonBefore,jsonAfter);
        return repository.save(dataInfo);
    }

    @Override
    public LoggerDataInfo findById(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<LoggerDataInfoDTO> findAll() {
        return LoggerDataInfoDTO.convertToDTO(repository.findAll(Sort.by(Sort.Direction.DESC,"date")));
    }

    @Override
    public void deleteByID(int id) {
        repository.deleteById(id);
    }
}
