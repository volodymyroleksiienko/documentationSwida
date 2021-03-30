package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.DescriptionDeskOak;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfProduct;

import java.util.List;

public interface PackagedProductService {
    void save(PackagedProduct packProd);
    void saveWithoutCalculating(PackagedProduct packProd);
    void createPackages(String dryStorageId, String codeOfProduct,String breedDescription,String countOfDesk, String countHeight, String countWidth,
                        String countOfPack, String longFact, String heightWidth, UserCompany userCompany);
    void createPackagesWithoutHistory(PackagedProduct product,String countOfPacks,int breedId, int userId);
    PackagedProduct createPackageOak(String[][] arrayOfDesk, String idOfDryStorage,
                          String codeOfPackage, String quality, String sizeOfHeight, String length,int userID,int breedID);
    PackagedProduct createPackageOak(List<DescriptionDeskOak> deskOakList, String idOfDryStorage,
                                            String codeOfPackage, String quality, String sizeOfHeight, String length, int userID, int breedID);
    PackagedProduct getProductByDryStorage(int dryStorageId);
    PackagedProduct findById(int id);
    String countExtent(PackagedProduct product);
    void countExtentOak(PackagedProduct product);
    String countDesk(PackagedProduct product);
    List<PackagedProduct> findAll();
    List<PackagedProduct> getListByUserByBreed(int breedId, int userId, StatusOfProduct status);
    void deleteByID(int id);
    PackagedProduct editPackageProduct(PackagedProduct product);
    PackagedProduct editPackageProductForImport(PackagedProduct product);
    PackagedProduct editPackageProductOak(PackagedProduct product);
    void addDescriptionOak(String packId, String width, String count);

    void setContainer(String[] arrayOfPackagesId, String containerId,String containerName);

    void deleteDescriptionOak(String packId, String deskId);
    void unformPackageProduct(int breedId,int userId,Integer[] id);

    //for statistic
    List<String> getListOfUnicBreedDescription(int breedId);
    List<String> getListOfUnicSizeOfHeight(int breedId);
    List<String> getListOfUnicSizeOfWidth(int breedId);
    List<String> getListOfUnicSizeOfLong(int breedId);

    List<String> getExtent(int breedId,String[] breedDesc,String[] sizeHeight,String[] sizeWidth,String[] sizeLong,int[] agentId,StatusOfProduct statusProduct);
    List<String> getExtentByOrder(int breedId, String[] breedDesc, String[] sizeHeight, String[] sizeWidth, String[] sizeLong, int[] agentId, DeliveryDestinationType type);

}
