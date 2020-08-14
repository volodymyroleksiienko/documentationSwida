package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.storages.DryingStorage;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.storages.RawStorage;
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

        deliveryDocumentation.setOrderInfo(orderInfo);
        deliveryDocumentation.setDestinationType(DeliveryDestinationType.valueOf(deliveryDestination));
        deliveryDocumentation.setDescription(description);

        deliveryDocumentation.setProductList(productList);
        deliveryDocumentation.setDriverInfo(driverInfo);

        driverInfoService.save(driverInfo);
        deliveryDocumentationService.save(deliveryDocumentation);
        return "redirect:/fabric/getListOfPackagedProduct-"+userID+"-"+breedID;
    }

    @PostMapping("/createPackageOakObject-{userID}-{breedID}")
    public void createPackageOak(@PathVariable("userID") int userID, @PathVariable("breedID") int breedID,
                                   @RequestParam("arrayOfDesk") String[][] arrayOfDesk, String idOfDryStorage,
                                   String codeOfPackage, String quality, String sizeOfHeight, String length){

        packagedProductService.createPackageOak(arrayOfDesk,idOfDryStorage,codeOfPackage,quality,sizeOfHeight,length,userID,breedID);
    }


}
