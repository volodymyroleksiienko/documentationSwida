package com.swida.documetation.data.serviceImpl.storage;


import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.DescriptionDeskOak;
import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.DryingStorage;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.jpa.storages.PackagedProductJPA;
import com.swida.documetation.data.service.storages.DescriptionDeskOakService;
import com.swida.documetation.data.service.storages.DryStorageService;
import com.swida.documetation.data.service.storages.PackagedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class PackagedProductServiceImpl implements PackagedProductService {
    private DryStorageService dryStorageService;
    private PackagedProductJPA productJPA;
    private DescriptionDeskOakService deskOakService;

    @Autowired
    public PackagedProductServiceImpl(DryStorageService dryStorageService, PackagedProductJPA productJPA, DescriptionDeskOakService deskOakService) {
        this.dryStorageService = dryStorageService;
        this.productJPA = productJPA;
        this.deskOakService = deskOakService;
    }

    @Override
    public void save(PackagedProduct packProd) {
        productJPA.save(packProd);
    }


    @Override
    public void createPackages(String dryStorageId, String codeOfProduct, String countHeight, String countWidth,
                               String countOfPack, String longFact,String heightWidth, UserCompany userCompany) {
        DryStorage dryStorage = dryStorageService.findById(Integer.parseInt(dryStorageId));
        for (int i=1; i<=Integer.parseInt(countOfPack);i++){
            PackagedProduct product = new PackagedProduct();
            product.setCodeOfPackage(codeOfProduct+"-"+i);
            product.setBreedOfTree(dryStorage.getBreedOfTree());
            product.setUserCompany(userCompany);
            product.setBreedDescription(dryStorage.getBreedDescription());
            product.setDryStorage(dryStorage);

            product.setSizeOfHeight(dryStorage.getSizeOfHeight());
            product.setSizeOfWidth(dryStorage.getSizeOfWidth());
            product.setSizeOfLong(dryStorage.getSizeOfLong());


            product.setCountOfDesk(String.valueOf(Integer.parseInt(countHeight)*Integer.parseInt(countWidth)));
            product.setCountDeskInHeight(countHeight);
            product.setCountDeskInWidth(countWidth);

            product.setLongFact(longFact);
            product.setExtent(countOfExtent(product));


            product.setHeight_width(heightWidth);


            dryStorage.setCountOfDesk(dryStorage.getCountOfDesk()-Integer.parseInt(product.getCountOfDesk()));

            productJPA.save(product);
        }
        dryStorageService.save(dryStorage);
    }

    @Override
    public void createPackageOak(String[][] arrayOfDesk, String idOfDryStorage, String codeOfPackage, String quality, String sizeOfHeight, String length,int userID,int breedID) {
        DryStorage dryStorage = dryStorageService.findById(Integer.parseInt(idOfDryStorage));

        UserCompany company = new UserCompany();
        company.setId(userID);
        BreedOfTree breedOfTree = new BreedOfTree();
        breedOfTree.setId(breedID);

        PackagedProduct product = new PackagedProduct();
        product.setUserCompany(company);
        product.setBreedOfTree(breedOfTree);
        product.setDryStorage(dryStorage);
        product.setCodeOfPackage(codeOfPackage);
        product.setQuality(quality);
        product.setSizeOfHeight(sizeOfHeight);
        product.setSizeOfLong(length);

        List<DescriptionDeskOak> deskOakList = new ArrayList<>();
        float cofExtent = Float.parseFloat(sizeOfHeight)*Float.parseFloat(length)/1000000;
        int sumWidth = 0;
        int countOfAllDesk = 0;
        float extent = 0;

        //i = 1 skip test obj
        for (int i=1; i<arrayOfDesk.length;i++){
            deskOakList.add(new DescriptionDeskOak(arrayOfDesk[i][0],arrayOfDesk[i][1]));
            sumWidth += Integer.parseInt(arrayOfDesk[i][0]);
            countOfAllDesk += Integer.parseInt(arrayOfDesk[i][1]);
            extent += (cofExtent*Float.parseFloat(arrayOfDesk[i][0])*Float.parseFloat(arrayOfDesk[i][1])/1000);
        }

        product.setDeskOakList(deskOakList);
        product.setSumWidthOfAllDesk(String.valueOf(sumWidth));
        product.setCountOfDesk(String.valueOf(countOfAllDesk));
        product.setExtent(String.valueOf(extent));

        dryStorage.setExtent(String.valueOf(Float.parseFloat(dryStorage.getExtent())-extent));
        dryStorageService.save(dryStorage);
        deskOakService.saveAll(deskOakList);
        productJPA.save(product);
    }

    @Override
    public PackagedProduct findById(int id) {
        return productJPA.getOne(id);
    }

    @Override
    public List<PackagedProduct> findAll() {
        return productJPA.findAll();
    }

    @Override
    public List<PackagedProduct> getListByUserByBreed(int breedId, int userId, StatusOfProduct status) {
        return productJPA.getListByUserByBreed(breedId,userId,status);
    }

    @Override
    public void deleteByID(int id) {
        productJPA.deleteById(id);
    }

    public String countOfExtent(PackagedProduct packagedProduct){
        float width = Float.parseFloat(packagedProduct.getSizeOfWidth())/1000;
        float height = Float.parseFloat(packagedProduct.getSizeOfHeight())/1000;
        float longSize = Float.parseFloat(packagedProduct.getSizeOfLong())/1000;
        int count = Integer.parseInt(packagedProduct.getCountOfDesk());
        float extent = width*height*longSize*count;
        return String.valueOf(extent);
    }
}
