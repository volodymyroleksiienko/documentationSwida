package com.swida.documetation.data.service.subObjects;

import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;

import java.util.List;

public interface DeliveryDocumentationService {
    void save(DeliveryDocumentation doc);
    DeliveryDocumentation findById(int id);
    List<DeliveryDocumentation> findAll();
    List<DeliveryDocumentation> getListByUserByBreed(int breedId, int userId);
    List<DeliveryDocumentation> getListByDistributionContractsId(List<Integer> contractId);
    void deleteByID(int id);
}
