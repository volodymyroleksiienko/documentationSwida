package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RawStorageJPA extends JpaRepository<RawStorage,Integer> {
    @Query("select r from  RawStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2 and r.countOfDesk>0")
    List<RawStorage> getListByUserByBreed(int breedId, int userId);
    @Query("select r from  RawStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2 and r.treeStorage.statusOfTreeStorage=?3")
    List<RawStorage> getListByUserByBreedByStatusOfTree(int breedId, int userId, StatusOfTreeStorage status);
    @Query("select r from  RawStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2 and r.extent<>'0.000' and  r.extent not like '-%'")
    List<RawStorage> getListByUserByBreedOak(int breedId, int userId);

    @Query("select r from  RawStorage  r where r.treeStorage.id=?1")
    List<RawStorage> findAllByTreeStorageId(int id);

    //selects for statistic

    @Query("select obj.breedDescription from RawStorage obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.breedDescription")
    List<String> getListOfUnicBreedDescription(int breedId);

    @Query("select obj.sizeOfHeight from RawStorage obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.sizeOfHeight")
    List<String> getListOfUnicSizeOfHeight(int breedId);

    @Query("select obj.sizeOfWidth from RawStorage obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.sizeOfWidth")
    List<String> getListOfUnicSizeOfWidth(int breedId);

    @Query("select obj.sizeOfLong from RawStorage obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.sizeOfLong")
    List<String> getListOfUnicSizeOfLong(int breedId);


    //select of extent
    @Query("select obj.extent from RawStorage obj where obj.breedOfTree.id=?1 and obj.breedDescription in ?2 and obj.sizeOfHeight in ?3 and obj.sizeOfWidth in ?4 and obj.sizeOfLong in ?5 and obj.userCompany.contrAgent.id in ?6 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%'")
    List<String> getExtent(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeWidth,String[] sizeLong,int[] agentId);

    @Query("select obj.extent from RawStorage obj where obj.breedOfTree.id=?1 and obj.breedDescription in ?2 and obj.sizeOfHeight in ?3 and obj.userCompany.contrAgent.id in ?4 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%'")
    List<String> getExtentOak(int breedId,String[] breedDesc,String[] sizeHeight,int[] agentId);
}
