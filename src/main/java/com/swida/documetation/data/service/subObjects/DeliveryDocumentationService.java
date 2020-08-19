package com.swida.documetation.data.service.subObjects;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.DeliveryDestinationType;

import java.util.List;

public interface DeliveryDocumentationService {
    void save(DeliveryDocumentation doc);
    void checkInfoFromImport(List<DeliveryDocumentation> docList, OrderInfo orderInfo);
    void deletePackageFromDeliveryDoc(PackagedProduct product);
    //    void checkInfoFromImportOak(List<DeliveryDocumentation> docList, OrderInfo orderInfo);
    DeliveryDocumentation findById(int id);
    List<DeliveryDocumentation> findAll();
    List<DeliveryDocumentation> getListByUserByBreed(int breedId, int userId);
    DeliveryDocumentation getDeliveryDocumentationByIdOfTruck(String idOfTruck);
    List<DeliveryDocumentation> getListByDistributionContractsId(List<Integer> contractId);
    List<DeliveryDocumentation> getListByDestinationType(DeliveryDestinationType type);
    List<String> getAllTruckIdList(List<DeliveryDocumentation> docList);
    void deleteByID(int id);
}
