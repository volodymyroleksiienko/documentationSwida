package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.entity.subObjects.DriverInfo;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.DryStorageService;
import com.swida.documetation.data.service.storages.DryingStorageService;
import com.swida.documetation.data.service.storages.PackagedProductService;
import com.swida.documetation.data.service.storages.RawStorageService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import com.swida.documetation.data.service.subObjects.DriverInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FabricRestController {
    DriverInfoService driverInfoService;
    PackagedProductService packagedProductService;
    DeliveryDocumentationService deliveryDocumentationService;
    OrderInfoService orderInfoService;
    UserCompanyService userCompanyService;
    RawStorageService rawStorageService;
    DryingStorageService dryingStorageService;
    DryStorageService dryStorageService;

    @Autowired
    public FabricRestController(DriverInfoService driverInfoService, PackagedProductService packagedProductService,
                                DeliveryDocumentationService deliveryDocumentationService, OrderInfoService orderInfoService,
                                UserCompanyService userCompanyService, RawStorageService rawStorageService,
                                DryingStorageService dryingStorageService, DryStorageService dryStorageService) {
        this.driverInfoService = driverInfoService;
        this.packagedProductService = packagedProductService;
        this.deliveryDocumentationService = deliveryDocumentationService;
        this.orderInfoService = orderInfoService;
        this.userCompanyService = userCompanyService;
        this.rawStorageService = rawStorageService;
        this.dryingStorageService = dryingStorageService;
        this.dryStorageService = dryStorageService;
    }


    @PostMapping("/createDeliveryDoc-{userID}-{breedID}")
    public String createDeliveryDoc(@PathVariable("userID") String userID, @PathVariable("breedID") String breedID,
            String[] list, String name, String phone, String idOfTruck, String numberOfTruck, String numberOfTrailer,
                                    String dateOfUnloading, String timeOfUnloading,String contractName, String deliveryDestination,
                                    String description) {
        ContrAgent userContrAgent = userCompanyService.findById(Integer.parseInt(userID)).getContrAgent();
        OrderInfo orderInfo = orderInfoService.findByCodeOfOrder(contractName);
        float extentOfAllPack = 0;

        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setFullName(name);
        driverInfo.setIdOfTruck(idOfTruck);
        driverInfo.setPhone(phone);
        driverInfo.setNumberOfTruck(numberOfTruck);
        driverInfo.setNumberOfTrailer(numberOfTrailer);

        List<PackagedProduct> productList = new ArrayList<>();
        DeliveryDocumentation deliveryDocumentation = new DeliveryDocumentation();
        BreedOfTree breedOfTree = new BreedOfTree();
        breedOfTree.setId(Integer.parseInt(breedID));
        deliveryDocumentation.setBreedOfTree(breedOfTree);
        deliveryDocumentation.setDateOfUnloading(dateOfUnloading);
        deliveryDocumentation.setTimeOfUnloading(timeOfUnloading);
        deliveryDocumentation.setContrAgent(userContrAgent);

        for (int i=0; i<list.length; i++){
            productList.add(packagedProductService.findById(Integer.parseInt(list[i])));
            productList.get(i).setStatusOfProduct(StatusOfProduct.IN_DELIVERY);
            productList.get(i).setOrderInfo(orderInfo.getMainOrder());
            packagedProductService.save(productList.get(i));
            extentOfAllPack += Float.parseFloat(productList.get(i).getExtent());
            if (i==0) {
                deliveryDocumentation.setUserCompany(productList.get(0).getUserCompany());
            }
        }

        deliveryDocumentation.setPackagesExtent(String.format("%.3f",extentOfAllPack).replace(",","."));

        deliveryDocumentation.setOrderInfo(orderInfo.getMainOrder());
        deliveryDocumentation.setDestinationType(DeliveryDestinationType.valueOf(deliveryDestination));
        deliveryDocumentation.setDescription(description);

        deliveryDocumentation.setProductList(productList);
        deliveryDocumentation.setDriverInfo(driverInfo);

        driverInfoService.save(driverInfo);
        deliveryDocumentationService.save(deliveryDocumentation);
        for(PackagedProduct product:productList){
            product.setDeliveryDocumentation(deliveryDocumentation);
            packagedProductService.save(product);
        }
        return "redirect:/fabric/getListOfPackagedProduct-"+userID+"-"+breedID;
    }

    @PostMapping("/createPackageOakObject-{userID}-{breedID}")
    public void createPackageOak(@PathVariable("userID") int userID, @PathVariable("breedID") int breedID,
                                   @RequestParam("arrayOfDesk") String[][] arrayOfDesk, String idOfDryStorage,
                                   String codeOfPackage, String quality, String sizeOfHeight, String length){

        packagedProductService.createPackageOak(arrayOfDesk,idOfDryStorage,codeOfPackage,quality,sizeOfHeight,length,userID,breedID);
    }

    @PostMapping("/createPackageOakObjectForExistDeliveryDoc")
    public void createPackageOakObjectForExistDeliveryDoc(String breedID,@RequestParam("arrayOfDesk") String[][] arrayOfDesk,
                                                          String deliveryId, String codeOfPackage, String quality,
                                                          String sizeOfHeight, String length){
        PackagedProduct product = packagedProductService.createPackageOak(arrayOfDesk,"",codeOfPackage,quality,sizeOfHeight,length,0,Integer.parseInt(breedID));

        DeliveryDocumentation documentation = deliveryDocumentationService.findById(Integer.parseInt(deliveryId));
        documentation.getProductList().add(product);
        deliveryDocumentationService.save(documentation);
    }

    @PostMapping("/createRawPackageOakObject-{userID}-{breedID}")
    public void createRawPackageOak(@PathVariable("userID") int userID, @PathVariable("breedID") int breedID,
                                 @RequestParam("arrayOfDesk") String[][] arrayOfDesk, String idOfDryStorage,
                                 String codeOfPackage, String quality, String sizeOfHeight, String length){
        float cofExtent = Float.parseFloat(sizeOfHeight)*Float.parseFloat(length)/1000000;
        float extent = 0;

        //i = 1 skip test obj
        for (int i=1; i<arrayOfDesk.length;i++){
            extent += (cofExtent*Float.parseFloat(arrayOfDesk[i][0])*Float.parseFloat(arrayOfDesk[i][1])/1000);
        }

        RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(idOfDryStorage));
        DryingStorage dryingStorage = dryingStorageService.createFromRawStorage(rawStorage);

        rawStorage.setExtent(String.format("%.3f",Float.parseFloat(rawStorage.getExtent())-extent).replace(',','.'));
        rawStorageService.save(rawStorage);

        DryStorage dryStorage = dryStorageService.createFromDryingStorage(dryingStorage);

        dryingStorage.setExtent("0.000");
        dryingStorage.setCodeOfProduct(dryingStorage.getCodeOfProduct()+"raw");
        dryingStorageService.save(dryingStorage);

        dryStorage.setExtent(String.format("%.3f",extent).replace(',','.'));
        dryStorage.setCodeOfProduct(dryStorage.getCodeOfProduct()+"raw");
        dryStorageService.save(dryStorage);

        packagedProductService.createPackageOak(arrayOfDesk,String.valueOf(dryStorage.getId()),codeOfPackage+"-raw",quality,sizeOfHeight,length,userID,breedID);
    }

}
