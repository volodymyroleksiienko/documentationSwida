package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.DryingStorage;

import java.math.BigDecimal;
import java.util.List;

public interface DryStorageService {
    DryStorage save(DryStorage ds);
    DryStorage findById(int id);
    DryStorage createFromDryingStorage(DryingStorage dryingStorage);
    DryStorage addDryStorageWithoutParent(int userId, int breedId,DryStorage dryStorage);

    List<DryStorage> findAll();
    List<DryStorage> getListByUserByBreed(int breedId, int userId);
    List<DryStorage> getFilteredList(int breedId, int userId,String[] descriptions,String[] heights,String[] longs,String[] widths);
    void countExtentRawStorageWithDeskDescription(DryStorage dryStorage);
    BigDecimal countExtent(List<DryStorage> dryStorage);
    void editDryStorage(DryStorage dryStorage);
    void deleteByID(int id);


    void collectToOnePineEntityDry(DryStorage dryStorage, Integer[] arrOfEntity, int userId, int breedId);
    void uncollectFromOnePineEntityDry(DryStorage dryStorage,int userId,int breedId);

    void collectToOneOakEntityDry(DryStorage dryStorage, Integer[] arrOfEntity, int userId, int breedId);

    //for statistic
    List<String> getListOfUnicBreedDescription(int breedId);
    List<String> getListOfUnicSizeOfHeight(int breedId);
    List<String> getListOfUnicSizeOfWidth(int breedId);
    List<String> getListOfUnicSizeOfLong(int breedId);

    List<String> getExtent(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeWidth,String[] sizeLong,int[] agentId);
    List<DryStorage> sortedBy(List<DryStorage> list,String sortedField, String sortedType);
}
