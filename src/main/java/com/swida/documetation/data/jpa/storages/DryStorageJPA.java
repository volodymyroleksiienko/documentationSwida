package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.DryingStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DryStorageJPA extends JpaRepository<DryStorage,Integer> {
    @Query("select r from  DryStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2 and r.countOfDesk>0 and r.statusOfEntity='ACTIVE'")
    List<DryStorage> getListByUserByBreed(int breedId, int userId);

    @Query("select r from  DryStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2 and  r.extent<>'0.000' and  r.extent not like '-%'  and r.statusOfEntity='ACTIVE'")
    List<DryStorage> getListByUserByBreedOak(int breedId, int userId);

    //selects for statistic

    @Query("select obj.breedDescription from DryStorage obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.breedDescription")
    List<String> getListOfUnicBreedDescription(int breedId);

    @Query("select obj.sizeOfHeight from DryStorage obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.sizeOfHeight")
    List<String> getListOfUnicSizeOfHeight(int breedId);

    @Query("select obj.sizeOfWidth from DryStorage obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.sizeOfWidth")
    List<String> getListOfUnicSizeOfWidth(int breedId);

    @Query("select obj.sizeOfLong from DryStorage obj where obj.sizeOfLong is not null and obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.sizeOfLong")
    List<String> getListOfUnicSizeOfLong(int breedId);

    //select of extent
    @Query("select obj.extent from DryStorage obj where obj.breedOfTree.id=?1 and obj.breedDescription in ?2 and obj.sizeOfHeight in ?3 and obj.sizeOfWidth in ?4 and obj.sizeOfLong in ?5 and obj.userCompany.contrAgent.id in ?6 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%'")
    List<String> getExtent(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeWidth,String[] sizeLong,int[] agentId);

    @Query("select obj.extent from DryStorage obj where obj.breedOfTree.id=?1 and obj.breedDescription in ?2 and obj.sizeOfHeight in ?3 and obj.sizeOfLong in ?4 and obj.userCompany.contrAgent.id in ?5 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%'")
    List<String> getExtentOak(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeOfLong,int[] agentId);
}
