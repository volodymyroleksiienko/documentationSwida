package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.DryingStorage;

import java.util.List;

public interface DryStorageService {
    void save(DryStorage ds);
    DryStorage findById(int id);
    DryStorage createFromDryingStorage(DryingStorage dryingStorage);
    List<DryStorage> findAll();
    List<DryStorage> getListByUserByBreed(int breedId, int userId);
    void countExtentRawStorageWithDeskDescription(DryStorage dryStorage);
    void editDryStorage(DryStorage dryStorage);
    void deleteByID(int id);

    //for statistic
    List<String> getListOfUnicBreedDescription(int breedId);
    List<String> getListOfUnicSizeOfHeight(int breedId);
    List<String> getListOfUnicSizeOfWidth(int breedId);
    List<String> getListOfUnicSizeOfLong(int breedId);

    List<String> getExtent(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeWidth,String[] sizeLong,int[] agentId);
}
