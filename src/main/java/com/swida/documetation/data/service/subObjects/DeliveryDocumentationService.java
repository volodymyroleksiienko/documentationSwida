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
    void checkInfoFromImportOak(List<DeliveryDocumentation> docList, OrderInfo orderInfo);
    DeliveryDocumentation findById(int id);
    List<DeliveryDocumentation> findAll();
    List<DeliveryDocumentation> getListByUserByBreed(int breedId, int userId);
    DeliveryDocumentation getDeliveryDocumentationByIdOfTruck(String idOfTruck);
    DeliveryDocumentation getDeliveryDocumentationByIdOfTruckByOrder(String idOfTruck,int orderId);
    List<DeliveryDocumentation> getListByDistributionContractsId(List<Integer> contractId);
    List<DeliveryDocumentation> getListByDestinationType(DeliveryDestinationType type);
    List<String> getAllTruckIdList(List<DeliveryDocumentation> docList);

    void editDeliveryDoc(DeliveryDocumentation documentation);
    void addPackageProductToDeliveryDoc(String docId,PackagedProduct product);
    void reloadExtentOfAllPack(DeliveryDocumentation documentation);
    void deletePackage(String id,String deliveryId);
    void deleteByID(int id);
}
