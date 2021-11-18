package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.DryingStorage;
import com.swida.documetation.data.entity.storages.RawStorage;

import java.util.List;
import java.util.Set;

public interface DryingStorageService {
    DryingStorage save(DryingStorage ds);
    DryingStorage findById(int id);
    DryingStorage createFromRawStorage(RawStorage rawStorage);
    List<DryingStorage> findAll();
    List<DryingStorage> getListByUserByBreed(int breedId, int userId);
    Set<Integer> getListCellByUserByBreed(int breedId, int userId);
    void countExtentRawStorageWithDeskDescription(DryingStorage dryingStorage);
    DryingStorage editDryingStorage(DryingStorage dryingStorage);
    void deleteByID(int id);

    //for statistic
    List<String> getListOfUnicBreedDescription(int breedId);
    List<String> getListOfUnicSizeOfHeight(int breedId);
    List<String> getListOfUnicSizeOfWidth(int breedId);
    List<String> getListOfUnicSizeOfLong(int breedId);

    List<String> getExtent(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeWidth,String[] sizeLong,int[] agentId);
}
