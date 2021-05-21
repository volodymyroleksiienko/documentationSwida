package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface QualityStatisticInfoJPA extends JpaRepository<QualityStatisticInfo,Integer> {
    QualityStatisticInfo findByTreeStorageIdAndAndHeight(int id,String height);

    @Query("select q from  QualityStatisticInfo  q where q.treeStorage.userCompany.id in ?1 and q.treeStorage.breedOfTree.id in ?2")
    List<QualityStatisticInfo> findByUserByBreed(List<Integer> userId,List<Integer> breedId);

    @Query("select q from  QualityStatisticInfo  q where q.treeStorage.userCompany.id in ?1 and q.treeStorage.breedOfTree.id in ?2 and q.rawStorage.sizeOfHeight in ?3 and trim(q.rawStorage.breedDescription) in ?4")
    List<QualityStatisticInfo> findByUserByBreedByHeightByDescription(List<Integer> userId,List<Integer> breedId,List<String> heights, List<String> description);

    @Transactional
    void deleteQualityStatisticInfoById(Integer integer);
}
