package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.DryingStorage;
import com.swida.documetation.data.entity.storages.RawStorage;

import java.math.BigDecimal;
import java.util.List;

public interface DryStorageService {
    DryStorage save(DryStorage ds);
    DryStorage findById(int id);
    List<DryStorage> findById(Integer[] idOfRows);
    DryStorage createFromDryingStorage(DryingStorage dryingStorage);
    DryStorage addDryStorageWithoutParent(int userId, int breedId,DryStorage dryStorage);

    List<DryStorage> findAll();
    List<DryStorage> getListByUserByBreed(int breedId, int userId);
    List<DryStorage> getFilteredList(int breedId, int userId,String[] descriptions,String[] heights,String[] longs,String[] widths);
    DryStorage countExtentRawStorageWithDeskDescription(DryStorage dryStorage);
    BigDecimal countExtent(List<DryStorage> dryStorage);
    DryStorage editDryStorage(DryStorage dryStorage);
    void deleteByID(int id);


    DryStorage collectToOnePineEntityDry(DryStorage dryStorage, Integer[] arrOfEntity, int userId, int breedId);
    List<DryStorage> uncollectFromOnePineEntityDry(DryStorage dryStorage,int userId,int breedId);

    DryStorage collectToOneOakEntityDry(DryStorage dryStorage, Integer[] arrOfEntity, int userId, int breedId);

    //for statistic
    List<String> getListOfUnicBreedDescription(int breedId);
    List<String> getListOfUnicSizeOfHeight(int breedId);
    List<String> getListOfUnicSizeOfWidth(int breedId);
    List<String> getListOfUnicSizeOfLong(int breedId);

    List<String> getExtent(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeWidth,String[] sizeLong,int[] agentId);
    List<DryStorage> sortedBy(List<DryStorage> list,String sortedField, String sortedType);
}
