package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.enums.StatusOfProduct;

import java.util.List;

public interface PackagedProductService {
    void save(PackagedProduct packProd);
    void saveWithoutCalculating(PackagedProduct packProd);
    void createPackages(String dryStorageId, String codeOfProduct,String breedDescription, String countHeight, String countWidth,
                        String countOfPack, String longFact, String heightWidth, UserCompany userCompany);
    void createPackageOak(String[][] arrayOfDesk, String idOfDryStorage,
                          String codeOfPackage, String quality, String sizeOfHeight, String length,int userID,int breedID);

    PackagedProduct findById(int id);
    String countExtent(PackagedProduct product);
    String countDesk(PackagedProduct product);
    List<PackagedProduct> findAll();
    List<PackagedProduct> getListByUserByBreed(int breedId, int userId, StatusOfProduct status);
    void deleteByID(int id);
    void editPackageProduct(PackagedProduct product);
}
