package com.swida.documetation.data.serviceImpl.storage;


import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.jpa.storages.PackagedProductJPA;
import com.swida.documetation.data.service.storages.DryStorageService;
import com.swida.documetation.data.service.storages.PackagedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PackagedProductServiceImpl implements PackagedProductService {
    private DryStorageService dryStorageService;
    private PackagedProductJPA productJPA;

    @Autowired
    public PackagedProductServiceImpl(DryStorageService dryStorageService, PackagedProductJPA productJPA) {
        this.dryStorageService = dryStorageService;
        this.productJPA = productJPA;
    }

    @Override
    public void save(PackagedProduct packProd) {
        productJPA.save(packProd);
    }


    @Override
    public void createPackages(String dryStorageId, String codeOfProduct, String countHeight, String countWidth,
                               String countOfPack, String longFact, UserCompany userCompany) {
        DryStorage dryStorage = dryStorageService.findById(Integer.parseInt(dryStorageId));
        for (int i=1; i<=Integer.parseInt(countOfPack);i++){
            PackagedProduct product = new PackagedProduct();
            product.setCodeOfPackage(codeOfProduct+"-"+i);
            product.setBreedOfTree(dryStorage.getBreedOfTree());
            product.setUserCompany(userCompany);
            product.setBreedDescription(dryStorage.getBreedDescription());

            product.setSizeOfHeight(dryStorage.getSizeOfHeight());
            product.setSizeOfWidth(dryStorage.getSizeOfWidth());
            product.setSizeOfLong(dryStorage.getSizeOfLong());


            product.setCountOfDesk(String.valueOf(Integer.parseInt(countHeight)*Integer.parseInt(countWidth)));
            product.setCountDeskInHeight(countHeight);
            product.setCountDeskInWidth(countWidth);

            product.setLongFact(longFact);
            product.setExtent(countOfExtent(product));


            dryStorage.setCountOfDesk(dryStorage.getCountOfDesk()-Integer.parseInt(product.getCountOfDesk()));

            productJPA.save(product);
        }
        dryStorageService.save(dryStorage);
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
