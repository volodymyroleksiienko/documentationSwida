package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PackagedProductJPA extends JpaRepository<PackagedProduct,Integer> {
    @Query("select r from  PackagedProduct  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2 and r.statusOfProduct=?3")
    List<PackagedProduct> getListByUserByBreed(int breedId, int userId, StatusOfProduct status);

    @Query("select obj from PackagedProduct obj where obj.dryStorage.id=?1 group by obj.dryStorage.id order by obj.id ")
    PackagedProduct getProductByDryStorage(int dryStorageId);

    //selects for statistic

    @Query("select obj.breedDescription from PackagedProduct obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.breedDescription")
    List<String> getListOfUnicBreedDescription(int breedId);

    @Query("select obj.sizeOfHeight from PackagedProduct obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.sizeOfHeight")
    List<String> getListOfUnicSizeOfHeight(int breedId);

    @Query("select obj.sizeOfWidth from PackagedProduct obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.sizeOfWidth")
    List<String> getListOfUnicSizeOfWidth(int breedId);

    @Query("select obj.sizeOfLong from PackagedProduct obj where obj.breedOfTree.id=?1 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%' group by obj.sizeOfLong")
    List<String> getListOfUnicSizeOfLong(int breedId);

    //select of extent
    @Query("select obj.extent from PackagedProduct obj where obj.breedOfTree.id=?1 and obj.breedDescription in ?2 and obj.sizeOfHeight in ?3 and obj.sizeOfWidth in ?4 and obj.sizeOfLong in ?5 and obj.userCompany.contrAgent.id in ?6 and obj.statusOfProduct=?7 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%'")
    List<String> getExtent(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeWidth,String[] sizeLong,int[] agentId,StatusOfProduct statusProduct);


    @Query("select obj.extent from PackagedProduct obj where obj.breedOfTree.id=?1 and obj.breedDescription in ?2 and obj.sizeOfHeight in ?3 and obj.sizeOfWidth in ?4 and obj.sizeOfLong in ?5 and obj.userCompany.contrAgent.id in ?6 and obj.orderInfo.destinationType=?7 and obj.statusOfEntity='ACTIVE' and obj.extent<>'0.000' and  obj.extent not like '-%'")
    List<String> getExtentByOrder(int breedId, String[] breedDesc, String[] sizeHeight, String[] sizeWidth, String[] sizeLong, int[] agentId, DeliveryDestinationType type);

}
