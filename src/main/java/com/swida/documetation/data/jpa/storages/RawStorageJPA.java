package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface RawStorageJPA extends JpaRepository<RawStorage,Integer> {
    @Query("select r from  RawStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2 and r.countOfDesk>0 and r.statusOfEntity='ACTIVE'")
    List<RawStorage> getListByUserByBreed(int breedId, int userId);
    @Query("select r from  RawStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2 and trim(r.breedDescription) in ?3 and r.sizeOfHeight in ?4 and r.sizeOfWidth in ?5 and r.sizeOfLong in ?6 " +
            "and r.countOfDesk>0 and r.statusOfEntity='ACTIVE'")
    List<RawStorage> getListByUserByBreed(int breedId, int userId, List<String> desc, List<String> height, List<String> width, List<String> longs);
    @Query("select r from  RawStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2 and trim(r.breedDescription) in ?3 and r.sizeOfHeight in ?4 and r.sizeOfLong in ?5 " +
            "and  r.extent<>'0.000' and  r.extent not like '-%'  and r.statusOfEntity='ACTIVE'")
    List<RawStorage> getListByUserByBreedOak(int breedId, int userId,List<String> desc,List<String> height,List<String> longs);

    @Query("select r from  RawStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2 and r.treeStorage.statusOfTreeStorage=?3")
    List<RawStorage> getListByUserByBreedByStatusOfTree(int breedId, int userId, StatusOfTreeStorage status);
    @Query("select r from  RawStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2 and r.extent<>'0.000' and  r.extent not like '-%' and r.statusOfEntity='ACTIVE'")
    List<RawStorage> getListByUserByBreedOak(int breedId, int userId);

    @Query("select r from  RawStorage  r where r.treeStorage.id=?1")
    List<RawStorage> findAllByTreeStorageId(int id);

    @Query("select r from  RawStorage  r where r.breedOfTree.id=?1 and r.userCompany.id=?2 and trim(r.breedDescription) like ?3 and r.sizeOfHeight like ?4 and r.sizeOfWidth like ?5 and r.sizeOfLong like ?6 and r.statusOfEntity='ACTIVE' and r.extent<>'0.000' and  r.extent not like '-%' ")
    List<RawStorage> findEqualRaw(int breedId, int userId, String desc,String heights,String widths,String longs);

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
    @Query("select obj.extent from RawStorage obj where obj.breedOfTree.id=?1 and trim(obj.breedDescription) in ?2 and obj.sizeOfHeight in ?3 and obj.sizeOfWidth in ?4 and obj.sizeOfLong in ?5 and obj.userCompany.contrAgent.id in ?6 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%'")
    List<String> getExtent(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeWidth,String[] sizeLong,int[] agentId);

    @Query("select obj.extent from RawStorage obj where obj.breedOfTree.id=?1 and trim(obj.breedDescription) in ?2 and obj.sizeOfHeight in ?3 and obj.userCompany.contrAgent.id in ?4 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%'")
    List<String> getExtentOak(int breedId,String[] breedDesc,String[] sizeHeight,int[] agentId);
}
