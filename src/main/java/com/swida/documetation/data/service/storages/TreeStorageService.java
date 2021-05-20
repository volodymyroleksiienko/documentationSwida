package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.enums.StatusOfTreeStorage;

import java.util.List;
import java.util.Map;

public interface TreeStorageService {
    TreeStorage save(TreeStorage ts);
    TreeStorage putNewTreeStorageObj(int breedId, int userId,TreeStorage treeStorage);
//    void checkQualityInfo(TreeStorage treeStorage,String height,float extent);
    TreeStorage getMainTreeStorage(int breedId, int userId);
    TreeStorage findById(int id);
    List<TreeStorage> findAll();
    List<TreeStorage> getListByUserByBreed(int breedId, int userId, StatusOfTreeStorage status);
    List<TreeStorage> getListByUserByBreedALL(int breedId, int userId, StatusOfTreeStorage status);
    List<String> getListOfUnicBreedDescription(int breedId);
    List<String> getListOfUnicBreedDescription(int breedId,int userId);
    Map<Integer, List<QualityStatisticInfo>> getQualityStatisticInfo(List<TreeStorage> treeStorageList);
    void deleteByID(int id);

    List<String> getListOfExtent(int breedId, String[] breedDesc,int[] providers,StatusOfTreeStorage status);
}
