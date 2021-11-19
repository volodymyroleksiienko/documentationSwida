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

import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
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
    public List<LoggerDataInfoDTO> findFiltered(String dateFrom, String dateTo, Integer[] breedId, Integer[] users, String[] storageType, String[] actions) {
        Date from,to;
        List<Integer> breedList,userList;
        List<StorageType> storageList;
        List<LoggerOperationType> operationList;
        from=null;
        to=null;
        breedList=null;
        userList=null;
        storageList=null;
        operationList=null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if(dateFrom!=null && !dateFrom.isEmpty()){
            try {
                from = format.parse(dateFrom+" 00:00:00");
            }catch (ParseException e){
                System.out.println(e.getMessage());
            }
        }
        if(dateTo!=null && !dateTo.isEmpty()){
            try {
                to = format.parse(dateTo+" 23:59:59");
            }catch (ParseException e){
                System.out.println(e.getMessage());
            }
        }
        if(breedId!=null && breedId.length>0){
            breedList = Arrays.asList(breedId);
        }
        if(users!=null && users.length>0){
            userList = Arrays.asList(users);
        }
        if(storageType!=null && storageType.length>0){
            storageList = StorageType.convert(storageType);
        }
        if(actions!=null && actions.length>0){
            operationList = LoggerOperationType.convert(actions);
        }
        List<LoggerDataInfo> dataInfo = repository.findFiltered(from,to,breedList,userList,storageList,operationList);
        return LoggerDataInfoDTO.convertToDTO(dataInfo);
    }

    @Override
    public void deleteByID(int id) {
        repository.deleteById(id);
    }
}
