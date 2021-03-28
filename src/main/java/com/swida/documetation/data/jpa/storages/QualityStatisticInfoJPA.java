package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QualityStatisticInfoJPA extends JpaRepository<QualityStatisticInfo,Integer> {
    QualityStatisticInfo findByTreeStorageIdAndAndHeight(int id,String height);

    @Query("select q from  QualityStatisticInfo  q where q.treeStorage.userCompany.id in ?1 and q.treeStorage.breedOfTree.id in ?2")
    List<QualityStatisticInfo> findByUserByBreed(List<Integer> userId,List<Integer> breedId);
}
