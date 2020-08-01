package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.enums.StatusOfProduct;

import java.util.List;

public interface PackagedProductService {
    void save(PackagedProduct packProd);
    void createPackages(String dryStorageId, String codeOfProduct, String countHeight, String countWidth,
                        String countOfPack, String longFact, UserCompany userCompany);
    PackagedProduct findById(int id);
    List<PackagedProduct> findAll();
    List<PackagedProduct> getListByUserByBreed(int breedId, int userId, StatusOfProduct status);
    void deleteByID(int id);
}
