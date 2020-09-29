package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.enums.StatusOfTreeStorage;

import java.util.List;

public interface RawStorageService {
    String save(RawStorage rs);
    RawStorage findById(int id);
    List<RawStorage> findAll();
    List<RawStorage> getListByUserByBreed(int breedId, int userId);
    List<RawStorage> getListByUserByBreedByStatusOfTree(int breedId, int userId, StatusOfTreeStorage status);
    void deleteByID(int id);

    //for statistic
    List<String> getListOfUnicBreedDescription(int breedId);
    List<String> getListOfUnicSizeOfHeight(int breedId);
    List<String> getListOfUnicSizeOfWidth(int breedId);
    List<String> getListOfUnicSizeOfLong(int breedId);

    List<String> getExtent(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeWidth,String[] sizeLong,int[] agentId);

}
