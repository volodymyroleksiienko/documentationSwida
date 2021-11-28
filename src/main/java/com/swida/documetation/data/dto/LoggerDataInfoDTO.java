package com.swida.documetation.data.dto;

import com.google.gson.Gson;
import com.swida.documetation.data.dto.storages.*;
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
public class LoggerDataInfoDTO<T1,T2> {
    private int id;
    private Date date;
    private BreedOfTreeDTO breedOfTree;
    private UserCompanyDTO user;
    private LoggerOperationType operationType;
    private StorageType storageType;
    private T1 objectBeforeChanging;
    private T2 objectAfterChanging;


    public static LoggerDataInfoDTO convertToDTO(LoggerDataInfo info){
        LoggerDataInfoDTO dto = new LoggerDataInfoDTO();
        if(info.getStorageType()==StorageType.TREE ) {
            dto = new LoggerDataInfoDTO<TreeStorageDTO,TreeStorageDTO>();
            dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), TreeStorageDTO.class);
            dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), TreeStorageDTO.class);
        }else if (info.getStorageType()==StorageType.TREE_STATISTIC && (info.getOperationType()==LoggerOperationType.UPDATING ||info.getOperationType()==LoggerOperationType.RETURNING) ){
            dto = new LoggerDataInfoDTO<QualityStatisticInfoDTO,QualityStatisticInfoDTO>();
            dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), QualityStatisticInfoDTO.class);
            dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), QualityStatisticInfoDTO.class);
        } else if (info.getStorageType()==StorageType.RAW && info.getOperationType()!=LoggerOperationType.GROUP_ITEMS && info.getOperationType()!=LoggerOperationType.UNGROUP_ITEMS){
            dto = new LoggerDataInfoDTO<RawStorageDTO,RawStorageDTO>();
            dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), RawStorageDTO.class);
            dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), RawStorageDTO.class);
        }else if (info.getStorageType()==StorageType.RAW && info.getOperationType()==LoggerOperationType.GROUP_ITEMS){
            dto = new LoggerDataInfoDTO<RawStorageListDTO,RawStorageDTO>();
            dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), RawStorageListDTO.class);
            dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), RawStorageDTO.class);
        }else if (info.getStorageType()==StorageType.RAW && info.getOperationType()==LoggerOperationType.UNGROUP_ITEMS){
            dto = new LoggerDataInfoDTO<RawStorageDTO,RawStorageListDTO>();
            dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), RawStorageDTO.class);
            dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), RawStorageListDTO.class);
        }else if (info.getStorageType()==StorageType.TREE_STATISTIC && info.getOperationType()==LoggerOperationType.CREATING){
            dto = new LoggerDataInfoDTO<QualityStatisticInfoListDTO,QualityStatisticInfoListDTO>();
            dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), QualityStatisticInfoListDTO.class);
            dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), QualityStatisticInfoListDTO.class);
        }else if (info.getStorageType()==StorageType.DRYING ){
            dto = new LoggerDataInfoDTO<DryingStorageDTO,DryingStorageDTO>();
            dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), DryingStorageDTO.class);
            dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), DryingStorageDTO.class);
        }else if (info.getStorageType()==StorageType.DRY ){
            if(info.getOperationType()==LoggerOperationType.SENDING) {
                dto = new LoggerDataInfoDTO<DryStorageListDTO, DryStorageListDTO>();
                dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), DryStorageListDTO.class);
                dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), DryStorageListDTO.class);
            }else if(info.getOperationType()==LoggerOperationType.GROUP_ITEMS) {
                dto = new LoggerDataInfoDTO<DryStorageListDTO, DryStorageDTO>();
                dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), DryStorageListDTO.class);
                dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), DryStorageDTO.class);
            }else if(info.getOperationType()==LoggerOperationType.UNGROUP_ITEMS) {
                dto = new LoggerDataInfoDTO<DryStorageDTO, DryStorageListDTO>();
                dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), DryStorageDTO.class);
                dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), DryStorageListDTO.class);
            }else {
                dto = new LoggerDataInfoDTO<DryStorageDTO, DryStorageDTO>();
                dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), DryStorageDTO.class);
                dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), DryStorageDTO.class);
            }
        }else if (info.getStorageType()==StorageType.PACKAGE ){
            if(info.getOperationType()==LoggerOperationType.RETURNING){
                dto = new LoggerDataInfoDTO<PackageProductListDTO, PackageProductListDTO>();
                dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), PackageProductListDTO.class);
                dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), PackageProductListDTO.class);
            }else if(info.getBreedOfTree().getId()==1 && (info.getOperationType()==LoggerOperationType.CREATING || info.getOperationType()==LoggerOperationType.SENDING)){
                dto = new LoggerDataInfoDTO<PackageProductListDTO, PackageProductListDTO>();
                dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), PackageProductListDTO.class);
                dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), PackageProductListDTO.class);
            }else {
                dto = new LoggerDataInfoDTO<PackagedProductDTO, PackagedProductDTO>();
                dto.objectBeforeChanging = new Gson().fromJson(info.getObjectBeforeChanging(), PackagedProductDTO.class);
                dto.objectAfterChanging = new Gson().fromJson(info.getObjectAfterChanging(), PackagedProductDTO.class);
            }
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
