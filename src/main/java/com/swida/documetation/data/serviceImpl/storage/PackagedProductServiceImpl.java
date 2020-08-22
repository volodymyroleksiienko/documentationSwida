package com.swida.documetation.data.serviceImpl.storage;


import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.jpa.storages.PackagedProductJPA;
import com.swida.documetation.data.service.storages.DescriptionDeskOakService;
import com.swida.documetation.data.service.storages.DryStorageService;
import com.swida.documetation.data.service.storages.PackagedProductService;
import com.swida.documetation.data.service.storages.RawStorageService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class PackagedProductServiceImpl implements PackagedProductService {
    private DryStorageService dryStorageService;
    private RawStorageService rawStorageService;
    private PackagedProductJPA productJPA;
    private DescriptionDeskOakService deskOakService;
    private BreedOfTreeService breedOfTreeService;

    @Autowired
    public PackagedProductServiceImpl(DryStorageService dryStorageService, PackagedProductJPA productJPA,
                                      DescriptionDeskOakService deskOakService, RawStorageService rawStorageService,
                                      BreedOfTreeService breedOfTreeService) {
        this.dryStorageService = dryStorageService;
        this.productJPA = productJPA;
        this.deskOakService = deskOakService;
        this.rawStorageService = rawStorageService;
    }

    @Override
    public void save(PackagedProduct packProd) {
        packProd.setExtent(String.format("%.3f", Float.parseFloat(packProd.getExtent().replace(',', '.'))).replace(',', '.'));
        Date date = new Date(System.currentTimeMillis());
        packProd.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        productJPA.save(packProd);
    }

    @Override
    public void saveWithoutCalculating(PackagedProduct packProd) {
        productJPA.save(packProd);
    }


    @Override
    public void createPackages(String dryStorageId, String codeOfProduct,String breedDescription, String countHeight, String countWidth,
                               String countOfPack, String longFact,String heightWidth, UserCompany userCompany) {
        DryStorage dryStorage = dryStorageService.findById(Integer.parseInt(dryStorageId));
        for (int i=1; i<=Integer.parseInt(countOfPack);i++){
            PackagedProduct product = new PackagedProduct();
            product.setCodeOfPackage(codeOfProduct+"-"+i);
            product.setBreedOfTree(dryStorage.getBreedOfTree());
            product.setBreedDescription(breedDescription);
            product.setUserCompany(userCompany);
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
            Date date = new Date(System.currentTimeMillis());
            product.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
            productJPA.save(product);
        }
        dryStorageService.save(dryStorage);
    }



    @Override
    public PackagedProduct createPackageOak(String[][] arrayOfDesk, String idOfDryStorage, String codeOfPackage, String quality, String sizeOfHeight, String length,int userID,int breedID) {
        DryStorage dryStorage = (idOfDryStorage.isEmpty())?null:dryStorageService.findById(Integer.parseInt(idOfDryStorage));

        UserCompany company = new UserCompany();
        company.setId(userID);
        BreedOfTree breedOfTree = new BreedOfTree();
        breedOfTree.setId(breedID);

        PackagedProduct product = new PackagedProduct();
        if(company.getId()!=0) {
            product.setUserCompany(company);
        }
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
            sumWidth += (Integer.parseInt(arrayOfDesk[i][0])*Integer.parseInt(arrayOfDesk[i][1]));
            countOfAllDesk += Integer.parseInt(arrayOfDesk[i][1]);
            extent += (cofExtent*Float.parseFloat(arrayOfDesk[i][0])*Float.parseFloat(arrayOfDesk[i][1])/1000);
        }

        product.setDeskOakList(deskOakList);
        product.setSumWidthOfAllDesk(String.valueOf(sumWidth));
        product.setCountOfDesk(String.valueOf(countOfAllDesk));
        product.setExtent(String.format("%.3f",extent).replace(',','.'));
        if(dryStorage!=null) {
            dryStorage.setExtent(String.format("%.3f", Float.parseFloat(dryStorage.getExtent()) - extent).replace(',', '.'));
            dryStorageService.save(dryStorage);
        }
        deskOakService.saveAll(deskOakList);
        Date date = new Date(System.currentTimeMillis());
        product.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        productJPA.save(product);

        return product;
    }

    @Override
    public PackagedProduct findById(int id) {
        return productJPA.getOne(id);
    }

    @Override
    public String countExtent(PackagedProduct product) {
        return String.format("%.3f",
                Float.parseFloat(countDesk(product))
                        *Float.parseFloat(product.getSizeOfHeight())
                        *Float.parseFloat(product.getSizeOfWidth())
                        *Float.parseFloat(product.getSizeOfLong())
                        /1000000000).replace(",",".");
    }

    @Override
    public void countExtentOak(PackagedProduct product) {
        int sumWidth = 0;
        int countOfAllDesk = 0;
        float extent = 0;

        float cofExtent = Float.parseFloat(product.getSizeOfHeight())*Float.parseFloat(product.getSizeOfLong())/1000000;

        for(DescriptionDeskOak deskOak: product.getDeskOakList()){
            sumWidth+=(Integer.parseInt(deskOak.getSizeOfWidth())* Integer.parseInt(deskOak.getCountOfDesk()));
            countOfAllDesk+= Integer.parseInt(deskOak.getCountOfDesk());
            extent +=( cofExtent* Float.parseFloat(deskOak.getSizeOfWidth())* Float.parseFloat(deskOak.getCountOfDesk()));
        }


        product.setSumWidthOfAllDesk(String.valueOf(sumWidth));
        product.setCountOfDesk(String.valueOf(countOfAllDesk));
        product.setExtent(String.format("%.3f",extent).replace(',','.'));
        productJPA.save(product);
    }

    @Override
    public String countDesk(PackagedProduct product) {
        return String.valueOf(Integer.parseInt(product.getCountDeskInWidth())*Integer.parseInt(product.getCountDeskInHeight())).replace(",",".");
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



    @Override
    public void editPackageProduct(PackagedProduct product) {
        PackagedProduct productDB = productJPA.getOne(product.getId());

        productDB.setCodeOfPackage(product.getCodeOfPackage());
        if (product.getBreedOfTree() != null) {
            productDB.setBreedOfTree(breedOfTreeService.getObjectByName(product.getBreedOfTree().getBreed()));
        }
        productDB.setBreedDescription(product.getBreedDescription());

        productDB.setSizeOfHeight(product.getSizeOfHeight());
        productDB.setSizeOfWidth(product.getSizeOfWidth());
        productDB.setSizeOfLong(product.getSizeOfLong());

        productDB.setCountOfDesk(product.getCountOfDesk());
        productDB.setExtent(product.getExtent());
        productDB.setCountDeskInHeight(product.getCountDeskInHeight());
        productDB.setCountDeskInWidth(product.getCountDeskInWidth());

        productDB.setLongFact(product.getLongFact());
        productDB.setHeight_width(product.getHeight_width());
        productJPA.save(productDB);
    }

    @Override
    public void addDescriptionOak(String packId, String width, String count) {
        DescriptionDeskOak deskOak = new DescriptionDeskOak();
        deskOak.setCountOfDesk(count);
        deskOak.setSizeOfWidth(width);
        deskOakService.save(deskOak);
        PackagedProduct product = productJPA.getOne(Integer.parseInt(packId));
        product.getDeskOakList().add(deskOak);
        productJPA.save(product);
    }

    @Override
    public void deleteDescriptionOak(String packId, String deskId) {
        PackagedProduct product = productJPA.getOne(Integer.parseInt(packId));
        DescriptionDeskOak deskOak = deskOakService.findById(Integer.parseInt(deskId));

        product.getDeskOakList().remove(deskOak);
        productJPA.save(product);
        deskOakService.deleteByID(deskOak.getId());
    }

    public String countOfExtent(PackagedProduct packagedProduct){
        float width = Float.parseFloat(packagedProduct.getSizeOfWidth())/1000;
        float height = Float.parseFloat(packagedProduct.getSizeOfHeight())/1000;
        float longSize = Float.parseFloat(packagedProduct.getSizeOfLong())/1000;
        int count = Integer.parseInt(packagedProduct.getCountOfDesk());
        float extent = width*height*longSize*count;
        return String.format("%.3f",extent).replace(',','.');
    }
}
