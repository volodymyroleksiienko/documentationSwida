package com.swida.documetation.data.jpa.subObjects;

import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryDocumentationJPA extends JpaRepository<DeliveryDocumentation,Integer> {
    @Query("select t from  DeliveryDocumentation  t where t.breedOfTree.id=?1 and  t.userCompany.id=?2 ")
    List<DeliveryDocumentation> getListByUserByBreed(int breedId, int userId);

    @Query("select t from  DeliveryDocumentation  t where t.orderInfo.id in ?1")
    List<DeliveryDocumentation> getListByDistributionContractsId(List<Integer> contractId);


    @Query("select t from DeliveryDocumentation  t where t.destinationType=?1")
    List<DeliveryDocumentation> getListByDestinationType(DeliveryDestinationType type);

    @Query("select obj from  DeliveryDocumentation obj where obj.driverInfo.idOfTruck=?1")
    DeliveryDocumentation getDeliveryDocumentationByIdOfTruck(String idOfTruck);

    @Query("select obj from  DeliveryDocumentation obj where obj.productList in ?1")
    DeliveryDocumentation getDeliveryDocumentationPackagedProduct(PackagedProduct product);
}
