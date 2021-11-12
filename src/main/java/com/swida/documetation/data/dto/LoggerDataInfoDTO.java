package com.swida.documetation.data.dto;

import com.google.gson.Gson;
import com.swida.documetation.data.dto.storages.QualityStatisticInfoDTO;
import com.swida.documetation.data.dto.storages.RawStorageDTO;
import com.swida.documetation.data.dto.storages.TreeStorageDTO;
import com.swida.documetation.data.dto.subObjects.BreedOfTreeDTO;
import com.swida.documetation.data.entity.LoggerDataInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.LoggerOperationType;
import com.swida.documetation.data.enums.StorageType;
import lombok.Data;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Data
public class LoggerDataInfoDTO<T> {
    private int id;
    private Date date;
    private BreedOfTreeDTO breedOfTree;
    private UserCompanyDTO user;
    private LoggerOperationType operationType;
    private StorageType storageType;
    private T objectBeforeChanging;
    private T objectAfterChanging;


    public static LoggerDataInfoDTO convertToDTO(LoggerDataInfo info){
        LoggerDataInfoDTO dto = new LoggerDataInfoDTO();
        if(info.getStorageType()==StorageType.TREE && info.getOperationType()==LoggerOperationType.UPDATING) {
            dto = new LoggerDataInfoDTO<TreeStorageDTO>();
            dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), TreeStorageDTO.class);
            dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), TreeStorageDTO.class);
        }else if (info.getStorageType()==StorageType.TREE_STATISTIC && info.getOperationType()==LoggerOperationType.UPDATING){
            dto = new LoggerDataInfoDTO<QualityStatisticInfoDTO>();
            dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), QualityStatisticInfoDTO.class);
            dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), QualityStatisticInfoDTO.class);
        } else if (info.getStorageType()==StorageType.RAW && info.getOperationType()==LoggerOperationType.CREATING){
        dto = new LoggerDataInfoDTO<RawStorageDTO>();
        dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), RawStorageDTO.class);
        dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), RawStorageDTO.class);
        }
        dto.id = info.getId();
        dto.date = info.getDate();
        dto.breedOfTree = BreedOfTreeDTO.convertToDTO(info.getBreedOfTree());
        dto.user = UserCompanyDTO.convertToDTO(info.getUser());
        dto.operationType = info.getOperationType();
        dto.storageType = info.getStorageType();

        return dto;
    }

    public static List<LoggerDataInfoDTO> convertToDTO(List<LoggerDataInfo> list){
        List<LoggerDataInfoDTO> dtoList = new ArrayList<>();
        for(LoggerDataInfo info: list){
            dtoList.add(convertToDTO(info));
        }
        return dtoList;
    }


}
