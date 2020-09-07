package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TreeStorageJPA extends JpaRepository<TreeStorage,Integer> {
    @Query("select t from  TreeStorage  t where t.breedOfTree.id=?1 and  t.userCompany.id=?2 and t.statusOfTreeStorage=?3 and  t.extent<>'0.000'")
    List<TreeStorage> getListByUserByBreed(int breedId, int userId, StatusOfTreeStorage status);

    @Query("select obj.breedDescription from TreeStorage obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.breedDescription")
    List<String> getListOfUnicBreedDescription(int breedId);

    @Query("select obj.extent from TreeStorage obj where obj.breedOfTree.id=?1 and obj.breedDescription in ?2 and obj.userCompany.contrAgent.id in ?3 and obj.statusOfTreeStorage=?4 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%'")
    List<String> getListOfExtent(int breedId, String[] breedDesc,int[] providers,StatusOfTreeStorage status);

}
