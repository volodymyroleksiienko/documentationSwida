package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.enums.StatusOfTreeStorage;

import java.math.BigDecimal;
import java.util.List;

public interface RawStorageService {
    String save(RawStorage rs);
    RawStorage findById(int id);
    List<RawStorage> findAll();
    List<RawStorage> findAllByTreeStorageId(int id);
    RawStorage findEqualRaw(int breedId, int userId, String desc,String heights,String widths,String longs);
    List<RawStorage> getListByUserByBreed(int breedId, int userId);
    List<RawStorage> getListByUserByBreedByStatusOfTree(int breedId, int userId, StatusOfTreeStorage status);
    List<RawStorage> getFilteredList(int breedId, int userId,String[] descriptions,String[] heights,String[] longs,String[] widths);
    void collectToOnePineEntity(RawStorage rawStorage,Integer[] arrOfEntity,int userId,int breedId);
    void uncollectFromOnePineEntity(RawStorage rawStorage,int userId,int breedId);
    void collectToOneOakEntity(RawStorage rawStorage,Integer[] arrOfEntity,int userId,int breedId);

    void checkQualityInfo(RawStorage rawStorage);
    void countExtentRawStorageWithDeskDescription(RawStorage rawStorage);
    BigDecimal countExtent(List<RawStorage> rawStorages);
    void deleteByID(int id);

    //for statistic
    List<String> getListOfUnicBreedDescription(int breedId);
    List<String> getListOfUnicSizeOfHeight(int breedId);
    List<String> getListOfUnicSizeOfWidth(int breedId);
    List<String> getListOfUnicSizeOfLong(int breedId);

    List<String> getExtent(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeWidth,String[] sizeLong,int[] agentId);

}
