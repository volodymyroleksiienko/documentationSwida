package com.swida.documetation.controllers;

import com.google.gson.Gson;
import com.swida.documetation.data.dto.TreeStorageListDto;
import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.entity.subObjects.DriverInfo;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.*;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import com.swida.documetation.data.service.subObjects.DriverInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    TreeStorageService treeStorageService;
    DescriptionDeskOakService deskOakService;

    @Autowired
    public FabricRestController(DriverInfoService driverInfoService, PackagedProductService packagedProductService,
                                DeliveryDocumentationService deliveryDocumentationService, OrderInfoService orderInfoService,
                                UserCompanyService userCompanyService, RawStorageService rawStorageService,
                                DryingStorageService dryingStorageService, DryStorageService dryStorageService,
                                TreeStorageService treeStorageService, DescriptionDeskOakService deskOakService ) {
        this.driverInfoService = driverInfoService;
        this.packagedProductService = packagedProductService;
        this.deliveryDocumentationService = deliveryDocumentationService;
        this.orderInfoService = orderInfoService;
        this.userCompanyService = userCompanyService;
        this.rawStorageService = rawStorageService;
        this.dryingStorageService = dryingStorageService;
        this.dryStorageService = dryStorageService;
        this.treeStorageService = treeStorageService;
        this.deskOakService = deskOakService;
    }


    @PostMapping("/createDeliveryDoc-{userID}-{breedID}")
    public String createDeliveryDoc(@PathVariable("userID") String userID, @PathVariable("breedID") String breedID,
            String[] list, String name, String phone, String idOfTruck, String numberOfTruck, String numberOfTrailer,
                                    String dateOfUnloading, String timeOfUnloading,String contractName, String deliveryDestination,
                                    String description) {
        ContrAgent userContrAgent = userCompanyService.findById(Integer.parseInt(userID)).getContrAgent();
        OrderInfo orderInfo = orderInfoService.findById(Integer.parseInt(contractName));
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
            productList.get(i).setOrderInfo(orderInfo);
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
        for(PackagedProduct product:productList){
            product.setDeliveryDocumentation(deliveryDocumentation);
            packagedProductService.save(product);
        }
        reloadAllExtentFields(deliveryDocumentation);
        deliveryDocumentationService.checkHeightUnicValue(deliveryDocumentation);
        return "redirect:/fabric/getListOfPackagedProduct-"+userID+"-"+breedID;
    }

    @PostMapping("/createPackageOakObject-{userID}-{breedID}")
    public String createPackageOak(@PathVariable("userID") int userID, @PathVariable("breedID") int breedID,
                                   @RequestParam("arrayOfDesk") String[][] arrayOfDesk, String idOfDryStorage,
                                   String codeOfPackage, String quality, String sizeOfHeight, String length){
        if (idOfDryStorage==null){
            idOfDryStorage="";
        }
        PackagedProduct product = packagedProductService.createPackageOak(arrayOfDesk,idOfDryStorage,codeOfPackage,quality,sizeOfHeight,length,userID,breedID);
        return (product.getDryStorage()!=null)?product.getDryStorage().getExtent():"0.000";
    }

    public void reloadAllExtentFields(DeliveryDocumentation deliveryDocumentation ){
        deliveryDocumentationService.reloadExtentOfAllPack(deliveryDocumentation);
        List<Integer> list = new ArrayList<>();
        list.add(deliveryDocumentation.getOrderInfo().getId());
        List<DeliveryDocumentation> docList = deliveryDocumentationService.getListByDistributionContractsId(list);
        orderInfoService.reloadOrderExtent(deliveryDocumentation.getOrderInfo(),docList);
        orderInfoService.reloadMainOrderExtent(deliveryDocumentation.getOrderInfo().getMainOrder());
    }

    @PostMapping("/createPackageOakObjectForExistDeliveryDoc")
    public void createPackageOakObjectForExistDeliveryDoc(String breedID,@RequestParam("arrayOfDesk") String[][] arrayOfDesk,
                                                          String deliveryId, String codeOfPackage, String quality,
                                                          String sizeOfHeight, String length){
        PackagedProduct product = packagedProductService.createPackageOak(arrayOfDesk,"",codeOfPackage,quality,sizeOfHeight,length,0,Integer.parseInt(breedID));

        DeliveryDocumentation documentation = deliveryDocumentationService.findById(Integer.parseInt(deliveryId));
        documentation.getProductList().add(product);
        deliveryDocumentationService.save(documentation);
        reloadAllExtentFields(documentation);
        deliveryDocumentationService.checkHeightUnicValue(documentation);

    }

//    @PostMapping("/createRawPackageOakObject-{userID}-{breedID}")
//    public String createRawPackageOak(@PathVariable("userID") int userID, @PathVariable("breedID") int breedID,
//                                 @RequestParam("arrayOfDesk") String[][] arrayOfDesk, String idOfDryStorage,
//                                 String codeOfPackage, String quality, String sizeOfHeight, String length){
//        float cofExtent = Float.parseFloat(sizeOfHeight)*Float.parseFloat(length)/1000000;
//        float extent = 0;
//
//        //i = 1 skip test obj
//        for (int i=1; i<arrayOfDesk.length;i++){
//            extent += (cofExtent*Float.parseFloat(arrayOfDesk[i][0])*Float.parseFloat(arrayOfDesk[i][1])/1000);
//        }
//
//        RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(idOfDryStorage));
//        DryingStorage dryingStorage = dryingStorageService.createFromRawStorage(rawStorage);
//
//        rawStorage.setExtent(String.format("%.3f",Float.parseFloat(rawStorage.getExtent())-extent).replace(',','.'));
//        rawStorageService.save(rawStorage);
//
//        DryStorage dryStorage = dryStorageService.createFromDryingStorage(dryingStorage);
//
//        dryingStorage.setExtent("0.000");
//        dryingStorage.setCodeOfProduct(dryingStorage.getCodeOfProduct()+"raw");
//        dryingStorageService.save(dryingStorage);
//
//        dryStorage.setExtent(String.format("%.3f",extent).replace(',','.'));
//        dryStorage.setCodeOfProduct(dryStorage.getCodeOfProduct()+"raw");
//        dryStorageService.save(dryStorage);
//
//        packagedProductService.createPackageOak(arrayOfDesk,String.valueOf(dryStorage.getId()),codeOfPackage+"-raw",quality,sizeOfHeight,length,userID,breedID);
//        return (rawStorage!=null)?rawStorage.getExtent():"0.000";
//    }


    @PostMapping("/createInitialPackageOakObject-{userID}-{breedID}")
    public void createInitialPackageOakObject(@PathVariable("userID") int userID, @PathVariable("breedID") int breedID,
                                              String codeOfPackage,String breedDescription,String supplier, String sizeOfHeight,
                                              String extent, String usedExtent, String treeStorageId,@RequestParam("arrayOfDesk") String[][] arrayOfDesk, String sizeOfLong){
        TreeStorage treeStorage = new TreeStorage();
        BreedOfTree breedOfTree = new BreedOfTree();
        breedOfTree.setId(breedID);
        UserCompany userCompany = new UserCompany();
        userCompany.setId(userID);

        if(treeStorageId.isEmpty()) {
            treeStorage.setCodeOfProduct(codeOfPackage);
            treeStorage.setBreedOfTree(breedOfTree);
            treeStorage.setUserCompany(userCompany);

            treeStorage.setBreedDescription(breedDescription);

            ContrAgent contrAgent = new ContrAgent();
            contrAgent.setId(Integer.parseInt(supplier));
            treeStorage.setContrAgent(contrAgent);
            treeStorage.setExtent("0.000");
            treeStorage.setStatusOfTreeStorage(StatusOfTreeStorage.PROVIDER_DESK);
            treeStorageService.save(treeStorage);
        }else {
            treeStorage = treeStorageService.findById(Integer.parseInt(treeStorageId));
            treeStorage.setExtent(
                    String.format("%.3f",Float.parseFloat(treeStorage.getExtent())-Float.parseFloat(usedExtent)).replace(",",".")
            );
            treeStorageService.save(treeStorage);
        }

        List<DescriptionDeskOak> descriptionDeskList = new ArrayList<>();

        RawStorage rawStorage = new RawStorage();
        rawStorage.setCodeOfProduct(codeOfPackage);
        rawStorage.setBreedOfTree(breedOfTree);
        rawStorage.setUserCompany(userCompany);

        rawStorage.setBreedDescription(breedDescription);
        rawStorage.setSizeOfHeight(sizeOfHeight);
        rawStorage.setSizeOfLong(sizeOfLong);
        rawStorage.setExtent(extent.replace(",","."));
        rawStorage.setMaxExtent(rawStorage.getExtent());
        rawStorage.setUsedExtent(usedExtent.replace(",","."));
        rawStorage.setTreeStorage(treeStorage);
//        rawStorage.setDeskOakList(descriptionDeskList);
        String rawExtent = rawStorageService.save(rawStorage).getExtent();
        if(treeStorage.getStatusOfTreeStorage()==StatusOfTreeStorage.PROVIDER_DESK){
            treeStorage.setMaxExtent(rawExtent);
        }

        for(int i=1;i<arrayOfDesk.length;i++){
            DescriptionDeskOak deskOak = new DescriptionDeskOak();
            deskOak.setSizeOfWidth(arrayOfDesk[i][0]);
            deskOak.setCountOfDesk(arrayOfDesk[i][1]);
            deskOak.setRawStorage(rawStorage);
            deskOakService.save(deskOak);
            descriptionDeskList.add(deskOak);
        }
        treeStorageService.save(treeStorage);
        if(supplier.isEmpty() || Integer.parseInt(supplier)==0) {
            rawStorageService.checkQualityInfo(rawStorage);
        }
    }

    @PostMapping("/createRawPackageOakObject-{userID}-{breedID}")
    public String createRawPackageOak(@PathVariable("userID") int userID, @PathVariable("breedID") int breedID,
                                      String idOfRawStorage, String extent){
       RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(idOfRawStorage));
       rawStorage.setExtent(
               String.format("%.3f",Float.parseFloat(rawStorage.getExtent())+Float.parseFloat(extent)).replace(",",".")
       );
       TreeStorage treeStorage = rawStorage.getTreeStorage();
       treeStorage.setExtent(
               String.format("%.3f",Float.parseFloat(treeStorage.getExtent())-Float.parseFloat(extent)).replace(",",".")
       );
        if(treeStorage.getStatusOfTreeStorage()==StatusOfTreeStorage.PROVIDER_DESK ){
            treeStorage.setMaxExtent(
                    String.format("%.3f",Float.parseFloat(treeStorage.getMaxExtent())+(Float.parseFloat(extent))).replace(",",".")
            );
            treeStorage.setExtent("0.000");
        }
       treeStorageService.save(treeStorage);
       rawStorageService.checkQualityInfo(rawStorage);
       rawStorageService.save(rawStorage);

       return rawStorage.getExtent();
    }

    @PostMapping("/createInitialPackageOakDryObject-{userID}-{breedID}")
    public void createInitialPackageOakDryObject(@PathVariable("userID") int userID, @PathVariable("breedID") int breedID,
                                      DryStorage dryStorage, @RequestParam("arrayOfDesk") String[][] arrayOfDesk){
        DryStorage dryStorageDB = dryStorageService.addDryStorageWithoutParent(userID,breedID,dryStorage);
        List<DescriptionDeskOak> deskOakList = new ArrayList<>();
        float cofExtent = Float.parseFloat(dryStorage.getSizeOfHeight())*Float.parseFloat(dryStorage.getSizeOfLong())/1000000;
        float extent = 0;

        //i = 1 skip test obj
        for (int i=1; i<arrayOfDesk.length;i++){
            DescriptionDeskOak deskOak = new DescriptionDeskOak(arrayOfDesk[i][0],arrayOfDesk[i][1]);
            deskOak.setDryStorage(dryStorageDB);
            deskOakList.add(deskOak);
            extent += (cofExtent*Float.parseFloat(arrayOfDesk[i][0])*Float.parseFloat(arrayOfDesk[i][1])/1000);
        }
        deskOakService.saveAll(deskOakList);
        dryStorageDB.setExtent(
                String.format("%.3f",extent).replace(",",".")
        );
        dryStorageService.save(dryStorageDB);
    }

    @PostMapping("/cutOfTreeStorageDTO")
    public void createInitialPackageOakDryObject(String dto) {
        TreeStorageListDto listDto = new Gson().fromJson(dto, TreeStorageListDto.class);
        System.out.println(listDto);
        rawStorageService.analyzeOfCutting(listDto);
    }

}
