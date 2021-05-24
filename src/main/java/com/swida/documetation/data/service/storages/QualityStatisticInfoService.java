package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;

import java.util.List;

public interface QualityStatisticInfoService {
    QualityStatisticInfo save(QualityStatisticInfo info);
    void edit(QualityStatisticInfo info);
    QualityStatisticInfo findById(int id);
    List<QualityStatisticInfo> findByUserByBreed(List<Integer> userId,List<Integer> breedId);
    QualityStatisticInfo findByTreeStorageIdAndAndHeight(int id,String height);
    List<QualityStatisticInfo> findByUserByBreedByHeightByDescription(List<Integer> userId,List<Integer> breedId,List<String> heights, List<String> description);
    List<QualityStatisticInfo> findAll();
    void returnQualityInfo(int id);
    void deleteByID(int id);
}
