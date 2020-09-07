package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.enums.StatusOfTreeStorage;

import java.util.List;

public interface TreeStorageService {
    void save(TreeStorage ts);
    void putNewTreeStorageObj(TreeStorage treeStorage);
    TreeStorage findById(int id);
    List<TreeStorage> findAll();
    List<TreeStorage> getListByUserByBreed(int breedId, int userId, StatusOfTreeStorage status);
    List<String> getListOfUnicBreedDescription(int breedId);
    void deleteByID(int id);

    List<String> getListOfExtent(int breedId, String[] breedDesc,int[] providers,StatusOfTreeStorage status);
}
