package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.entity.subObjects.DriverInfo;
import com.swida.documetation.data.enums.StatusOfOrderInfo;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.*;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import com.swida.documetation.data.service.subObjects.DriverInfoService;
import com.swida.documetation.utils.other.GenerateResponseForExport;
import com.swida.documetation.utils.xlsParsers.*;
import org.apache.tomcat.util.digester.ObjectCreateRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.List;

@RequestMapping("/fabric")
@Controller

public class FabricController {
    private TreeStorageService treeStorageService;
    private RawStorageService rawStorageService;
    private DryingStorageService dryingStorageService;
    private DryStorageService dryStorageService;
    private PackagedProductService packagedProductService;
    private DeliveryDocumentationService deliveryDocumentationService;
    private BreedOfTreeService breedOfTreeService;
    private ContrAgentService contrAgentService;
    private UserCompanyService userCompanyService;
    private OrderInfoService orderInfoService;
    private DriverInfoService driverInfoService;

    @Autowired
    public FabricController(TreeStorageService treeStorageService, RawStorageService rawStorageService,
                            DryingStorageService dryingStorageService, DryStorageService dryStorageService,
                            PackagedProductService packagedProductService, DeliveryDocumentationService deliveryDocumentationService,
                            BreedOfTreeService breedOfTreeService, ContrAgentService contrAgentService,
                            UserCompanyService userCompanyService, OrderInfoService orderInfoService,
                            DriverInfoService driverInfoService) {
        this.treeStorageService = treeStorageService;
        this.rawStorageService = rawStorageService;
        this.dryingStorageService = dryingStorageService;
        this.dryStorageService = dryStorageService;
        this.packagedProductService = packagedProductService;
        this.deliveryDocumentationService = deliveryDocumentationService;
        this.breedOfTreeService = breedOfTreeService;
        this.contrAgentService = contrAgentService;
        this.userCompanyService = userCompanyService;
        this.orderInfoService = orderInfoService;
        this.driverInfoService = driverInfoService;
    }

    @GetMapping("/index-{userId}")
    public String index(@PathVariable("userId")int userId){

        int breedId = 1;
        return "redirect:/fabric/getListOfOrders-"+userId+"-"+breedId;
    }

    //    Orders page
    @GetMapping("/getListOfOrders-{id}-{breedId}")
    public String getListOfOrders(@PathVariable("id")int userId,@PathVariable("breedId")int breedId,
                                  Model model){
        UserCompany company = userCompanyService.findById(userId);
        ContrAgent contrAgent = company.getContrAgent();

        model.addAttribute("fragmentPathOrderInfo","orders");
        model.addAttribute("tabName","orders");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("orderInfoList",orderInfoService.getOrdersListByAgent(contrAgent.getId()));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));


        return "fabricPage";
    }


    //Tree Storage page
    @GetMapping("/getListOfTreeStorage-{userId}-{breedId}")
    public String getListOfTreeStorage(@PathVariable("userId")int userId,
                                       @PathVariable("breedId")int breedId, Model model){
        model.addAttribute("fragmentPathTabTreeStorage","treeStorage");
        model.addAttribute("tabName","treeStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("contrAgentList",contrAgentService.findAll());
        model.addAttribute("treeStorageList",treeStorageService.getListByUserByBreed(breedId,userId, StatusOfTreeStorage.TREE));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        return "fabricPage";
    }

    @PostMapping("/addTreeStorageRow-{userId}-{breedId}")
    public String  addTreeStorageObj(@PathVariable("userId")int userId,
                                     @PathVariable("breedId")int breedId,
                                     String nameOfAgent, TreeStorage treeStorage){
        BreedOfTree breedOfTree = new BreedOfTree();
        breedOfTree.setId(breedId);
        treeStorage.setBreedOfTree(breedOfTree);
        UserCompany company = new UserCompany();
        company.setId(userId);
        treeStorage.setUserCompany(company);
        ContrAgent contrAgent =  new ContrAgent();
        contrAgent.setNameOfAgent(nameOfAgent);
        treeStorage.setContrAgent(contrAgent);
        treeStorage.setExtent(String.format("%.3f", Float.parseFloat(treeStorage.getExtent())).replace(',', '.'));
        treeStorageService.putNewTreeStorageObj(treeStorage);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }


    @PostMapping("/cutOfTreeStorage-{userId}-{breedId}")
    public String  addCutTreeToRawStorage(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                       int idOfTreeStorageRow, String extentOfTreeStorage, RawStorage rawStorage,String  extentOfWaste){
        TreeStorage treeStorage = treeStorageService.findById(idOfTreeStorageRow);
        String mainExtentTreeStorage = treeStorage.getExtent();

        treeStorage.setExtent(String.format("%.3f",Float.parseFloat(extentOfTreeStorage)).replace(',','.'));
        rawStorage.setTreeStorage(treeStorage);
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setBreedDescription(treeStorage.getBreedDescription());

        TreeStorage recycle = new TreeStorage();
        if(extentOfWaste==null){
            recycle.setExtent("0.000");
        }else {
            recycle.setExtent(String.format("%.3f", Float.parseFloat(extentOfWaste)).replace(',', '.'));
        }
        recycle.setCodeOfProduct(treeStorage.getCodeOfProduct()+"-rec");
        recycle.setStatusOfTreeStorage(StatusOfTreeStorage.RECYCLING);
        recycle.setContrAgent(treeStorage.getContrAgent());

        recycle.setUserCompany(userCompanyService.findById(userId));
        recycle.setBreedOfTree(breedOfTreeService.findById(breedId));
        recycle.setBreedDescription(treeStorage.getBreedDescription());

        treeStorageService.save(recycle);
        rawStorageService.save(rawStorage);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editTreeStorageRow-{userId}-{breedId}")
    public String editTreeStorageRow(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                     String nameOfAgent, TreeStorage treeStorage){
        treeStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        treeStorage.setUserCompany(userCompanyService.findById(userId));
        ContrAgent contrAgent =  new ContrAgent();
        contrAgent.setNameOfAgent(nameOfAgent);
        treeStorage.setContrAgent(contrAgent);
        treeStorage.setExtent(String.format("%.3f", Float.parseFloat(treeStorage.getExtent())).replace(',', '.'));
        treeStorageService.putNewTreeStorageObj(treeStorage);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/exportTreeStorageXLS-{userId}-{breedId}")
    public ResponseEntity<Resource> exportTreeStorageXLS(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, String startDate,
                                                         String endDate) throws FileNotFoundException, ParseException {
        ParseTreeStorageToXLS parser = new ParseTreeStorageToXLS(treeStorageService.getListByUserByBreed(breedId,userId, StatusOfTreeStorage.TREE));
        String filePath = parser.parse(startDate,endDate);

        return new GenerateResponseForExport().generate(filePath,startDate,endDate);
    }


    @PostMapping("/addDeskFromProvider-{userId}-{breedId}")
    public String addDeskFromProvider(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                      TreeStorage treeStorage, String sizeOfHeight, String sizeOfWidth,
                                      String sizeOfLong, String countOfDesk, String nameOfAgent, String extent){
        treeStorage.setUserCompany(userCompanyService.findById(userId));
        treeStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        treeStorage.setExtent("0.000");
        ContrAgent contrAgent =  new ContrAgent();
        contrAgent.setNameOfAgent(nameOfAgent);
        treeStorage.setContrAgent(contrAgent);
        treeStorage.setStatusOfTreeStorage(StatusOfTreeStorage.PROVIDER_DESK);
        treeStorageService.putNewTreeStorageObj(treeStorage);

        RawStorage rawStorage = new RawStorage();
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setTreeStorage(treeStorage);

        rawStorage.setCodeOfProduct(treeStorage.getCodeOfProduct());
        rawStorage.setBreedDescription(treeStorage.getBreedDescription());
        rawStorage.setExtent(treeStorage.getExtent());

        rawStorage.setSizeOfHeight(sizeOfHeight);
        rawStorage.setSizeOfWidth(sizeOfWidth);
        rawStorage.setSizeOfLong(sizeOfLong);
        rawStorage.setCountOfDesk(Integer.parseInt(countOfDesk));

        rawStorageService.save(rawStorage);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }


    //RawStorage page

    @GetMapping("/getListOfRawStorage-{userId}-{breedId}")
    public String getListOfRawStorage(@PathVariable("userId")int userId,
                                      @PathVariable("breedId")int breedId, Model model){
        model.addAttribute("fragmentPathTabRawStorage","rawStorage");
        model.addAttribute("tabName","rawStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("rawStorageList",rawStorageService.getListByUserByBreed(breedId, userId));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return "fabricPage";
    }


    @PostMapping("/addDeskToDrying-{userId}-{breedId}")
    private String addDeskToDrying(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    String rawStorageId, DryingStorage dryingStorage){
        dryingStorage.setUserCompany(userCompanyService.findById(userId));
        dryingStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(rawStorageId));
        rawStorage.setCountOfDesk(rawStorage.getCountOfDesk()-dryingStorage.getCountOfDesk());
        dryingStorage.setSizeOfWidth(rawStorage.getSizeOfWidth());
        dryingStorage.setSizeOfHeight(rawStorage.getSizeOfHeight());
        dryingStorage.setSizeOfLong(rawStorage.getSizeOfLong());
        rawStorageService.save(rawStorage);
        dryingStorage.setRawStorage(rawStorage);
        dryingStorageService.save(dryingStorage);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editRawStorageObj-{userId}-{breedId}")
    public String editRawStorageObj(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    RawStorage rawStorage){

        RawStorage rawStorageDB = rawStorageService.findById(rawStorage.getId());
        rawStorageDB.setBreedDescription(rawStorage.getBreedDescription());
        rawStorageDB.setCodeOfProduct(rawStorage.getCodeOfProduct());
        rawStorageDB.setCountOfDesk(rawStorage.getCountOfDesk());
        rawStorageDB.setSizeOfHeight(rawStorage.getSizeOfHeight());
        rawStorageDB.setSizeOfLong(rawStorage.getSizeOfLong());
        rawStorageDB.setSizeOfWidth(rawStorage.getSizeOfWidth());
        rawStorageService.save(rawStorageDB);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/exportRawStorageXLS-{userId}-{breedId}")
    public ResponseEntity<Resource> exportRawStorageXLS(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, String startDate,
                                                         String endDate) throws FileNotFoundException, ParseException {
        ParseRawStorageToXLS parser = new ParseRawStorageToXLS(rawStorageService.getListByUserByBreed(breedId,userId));
        String filePath;
        if (breedId==2){
//            for breed oak
             filePath = parser.parseOAK(startDate,endDate);
        }else {
             filePath = parser.parse(startDate,endDate);
        }
       return new GenerateResponseForExport().generate(filePath,startDate,endDate);
    }


    @PostMapping("/createRawPackages-{userId}-{breedId}")
    public String createRawPackages(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,String id,
                                 String codeOfProduct,String breedDescription,String height, String width, String count, String longFact, String heightWidth){
        int countOfDesk = Integer.parseInt(width)*Integer.parseInt(height)*Integer.parseInt(count);

        RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(id));
        DryingStorage dryingStorage = dryingStorageService.createFromRawStorage(rawStorage);

        rawStorage.setCountOfDesk(rawStorage.getCountOfDesk()-countOfDesk);
        rawStorageService.save(rawStorage);

        DryStorage dryStorage = dryStorageService.createFromDryingStorage(dryingStorage);

        dryingStorage.setCountOfDesk(0);
        dryingStorage.setCodeOfProduct(dryingStorage.getCodeOfProduct()+"raw");
        dryingStorageService.save(dryingStorage);

        dryStorage.setCountOfDesk(countOfDesk);
        dryStorage.setCodeOfProduct(dryStorage.getCodeOfProduct()+"raw");
        dryStorageService.save(dryStorage);

        packagedProductService.createPackages(String.valueOf(dryStorage.getId()),codeOfProduct+"raw",breedDescription,height,width,count,longFact,heightWidth,userCompanyService.findById(userId));
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    //Drying page
    @GetMapping("/getListOfDryingStorage-{userId}-{breedId}")
    public String getListOfDryingStorage(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,Model model){
        model.addAttribute("fragmentPathTabDryingStorage","dryingStorage");
        model.addAttribute("tabName","dryingStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("dryingStorageList",dryingStorageService.getListByUserByBreed(breedId,userId));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return "fabricPage";
    }

    @PostMapping("/addDeskToDryStorage-{userId}-{breedId}")
    private String addDeskToDryStorage(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                   String dryingStorageId, String codeOfProduct, String breedDescription){

        DryingStorage dryingStorageDB = dryingStorageService.findById(Integer.parseInt(dryingStorageId));
        DryStorage dryStorage = new DryStorage();
        dryStorage.setCodeOfProduct(codeOfProduct);

        dryStorage.setBreedOfTree(dryingStorageDB.getBreedOfTree());
        dryStorage.setBreedDescription(breedDescription);
        dryStorage.setCountOfDesk(dryingStorageDB.getCountOfDesk());
        dryStorage.setDescription(dryingStorageDB.getDescription());
        dryStorage.setExtent(dryingStorageDB.getExtent());
        dryStorage.setSizeOfHeight(dryingStorageDB.getSizeOfHeight());
        dryStorage.setSizeOfLong(dryingStorageDB.getSizeOfLong());
        dryStorage.setSizeOfWidth(dryingStorageDB.getSizeOfWidth());
        dryStorage.setDescription(dryingStorageDB.getDescription());
        dryStorage.setUserCompany(dryingStorageDB.getUserCompany());
        dryStorage.setDryingStorage(dryingStorageDB);

        dryStorageService.save(dryStorage);
        dryingStorageDB.setCountOfDesk(0);
        dryingStorageService.save(dryingStorageDB);
        return "redirect:/fabric/getListOfDryingStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editDryingStorageObj-{userId}-{breedId}")
    public String editDryingStorageObj(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    DryingStorage dryingStorage ){

        DryingStorage dryingStorageDB = dryingStorageService.findById(dryingStorage.getId());
        if(dryingStorageDB.getCountOfDesk()!=dryingStorage.getCountOfDesk()){
            int count = dryingStorageDB.getCountOfDesk()-dryingStorage.getCountOfDesk();
            RawStorage rawStorage = dryingStorageDB.getRawStorage();
            rawStorage.setCountOfDesk(rawStorage.getCountOfDesk()+count);
            rawStorageService.save(rawStorage);
        }
        dryingStorageDB.setBreedDescription(dryingStorage.getBreedDescription());
        dryingStorageDB.setCodeOfProduct(dryingStorage.getCodeOfProduct());
        dryingStorageDB.setCountOfDesk(dryingStorage.getCountOfDesk());
        dryingStorageService.save(dryingStorageDB);
        return "redirect:/fabric/getListOfDryingStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/exportDryingStorageXLS-{userId}-{breedId}")
    public ResponseEntity<Resource> exportDryingStorageXLS(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, String startDate,
                                                        String endDate) throws FileNotFoundException, ParseException {
        ParseDryingToXLS parser = new ParseDryingToXLS(dryingStorageService.getListByUserByBreed(breedId,userId));
        String filePath;
        if (breedId==2){
//            for breed oak
            filePath = parser.parseOAK(startDate,endDate);
        }else {
            filePath = parser.parse(startDate,endDate);
        }
        return new GenerateResponseForExport().generate(filePath,startDate,endDate);
    }

    //Dry Storage page
    @GetMapping("/getListOfDryStorage-{userId}-{breedId}")
    public String getListOfDryStorage(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                      Model model){
        model.addAttribute("fragmentPathTabDryStorage","dryStorage");
        model.addAttribute("tabName","dryStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("dryStorageList",dryStorageService.getListByUserByBreed(breedId,userId));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return "fabricPage";
    }

    @PostMapping("/editDryStorageRow-{userId}-{breedId}")
    public String editDryStorageRow(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,String id,
                                  String codeOfProduct, String breedDescription){
        DryStorage dryStorage = dryStorageService.findById(Integer.parseInt(id));
        dryStorage.setBreedDescription(breedDescription);
        dryStorage.setCodeOfProduct(codeOfProduct);
        dryStorageService.save(dryStorage);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/createPackages-{userId}-{breedId}")
    public String createPackages(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,String id,
                                    String codeOfProduct,String breedDescription,String height, String width, String count, String longFact, String heightWidth){
        packagedProductService.createPackages(id,codeOfProduct,breedDescription,height,width,count,longFact,heightWidth,userCompanyService.findById(userId));


        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/exportDryStorageXLS-{userId}-{breedId}")
    public ResponseEntity<Resource> exportDryStorageXLS(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, String startDate,
                                                           String endDate) throws FileNotFoundException, ParseException {
        ParseDryStorageToXLS parser = new ParseDryStorageToXLS(dryStorageService.getListByUserByBreed(breedId,userId));
        String filePath;
        if (breedId==2){
//            for breed oak
            filePath = parser.parseOAK(startDate,endDate);
        }else {
            filePath = parser.parse(startDate,endDate);
        }
        return new GenerateResponseForExport().generate(filePath,startDate,endDate);
    }


    //Packaged product page
    @GetMapping("/getListOfPackagedProduct-{userId}-{breedId}")
    public String getListOfPackagedProduct(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,Model model){
        UserCompany company = userCompanyService.findById(userId);
        ContrAgent contrAgent = company.getContrAgent();

        model.addAttribute("fragmentPathTabPackageStorage","packageStorage");
        model.addAttribute("tabName","packageStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);

        model.addAttribute("contractList",orderInfoService.getOrdersListByAgent(contrAgent.getId()));
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("packagedProductsList",packagedProductService.getListByUserByBreed(breedId,userId, StatusOfProduct.ON_STORAGE));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("deliveryList",deliveryDocumentationService.getListByUserByBreed(breedId,userId));

        return "fabricPage";
    }

    @PostMapping("/unformPackagedProduct-{userId}-{breedId}")
    public  String unformPackagedProduct(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, String id){

        PackagedProduct product = packagedProductService.findById(Integer.parseInt(id));

        DryStorage dryStorage = product.getDryStorage();

        dryStorage.setCountOfDesk(dryStorage.getCountOfDesk()+ Integer.parseInt(product.getCountOfDesk()));
        dryStorageService.save(dryStorage);
        packagedProductService.deleteByID(Integer.parseInt(id));

        return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+breedId;
    }

    @PostMapping("/addPackToExistDeliveryDoc-{userId}-{breedId}")
    public String addPackToExistDeliveryDoc(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                       String packId,String driverId){
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(packId));
        DeliveryDocumentation deliveryDocumentation = deliveryDocumentationService.getDeliveryDocumentationByIdOfTruck(driverId);
        product.setStatusOfProduct(StatusOfProduct.IN_DELIVERY);
        packagedProductService.save(product);
        deliveryDocumentation.getProductList().add(product);
        deliveryDocumentationService.save(deliveryDocumentation);

        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }

    @PutMapping("/addPackagedProductToDelivery")
    public void addPackagedProductToDelivery(DriverInfo driverInfo,List<PackagedProduct> productList){
        DeliveryDocumentation deliveryDocumentation = new DeliveryDocumentation();
        deliveryDocumentation.setDriverInfo(driverInfo);
        deliveryDocumentation.setProductList(productList);
        deliveryDocumentationService.save(deliveryDocumentation);
    }

    //Delivery page
    @GetMapping("/getListOfDeliveryDocumentation-{userId}-{breedId}")
    public String getListOfDeliveryDocumentation(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                                 Model model){
        UserCompany company = userCompanyService.findById(userId);
        ContrAgent contrAgent = company.getContrAgent();
        model.addAttribute("fragmentPathTabDelivery","deliveryInfo");
        model.addAttribute("tabName","deliveryInfo");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("deliveryDocumentations",deliveryDocumentationService.getListByUserByBreed(breedId,userId));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("contractList",orderInfoService.getOrdersListByAgent(contrAgent.getId()));

        model.addAttribute("urlEditDriver","/fabric/editDeliveryDocumentation-"+userId+"-"+breedId);
        model.addAttribute("urlEditPackage","/fabric/editPackageProduct-"+userId+"-"+breedId);
        model.addAttribute("urlAddPackage","/fabric/addPackageProduct-"+userId+"-"+breedId);
        model.addAttribute("urlDeletePackage","/fabric/deletePackageProduct-"+userId+"-"+breedId);

        return "fabricPage";
    }

    @PostMapping("/exportDeliveryInfo-{id}")
    public ResponseEntity<Resource> exportDeliveryInfo(@PathVariable("id") int id) throws FileNotFoundException {
        DeliveryDocumentation deliveryDocumentation = deliveryDocumentationService.findById(id);
        String filePath;
        if(deliveryDocumentation.getBreedOfTree().getId()==2){
            ParseOakDeliveryInfoToXLS parser = new ParseOakDeliveryInfoToXLS(deliveryDocumentation);
            filePath = parser.parse();
        }else {
            ParserDeliveryDocumentationToXLS parser = new ParserDeliveryDocumentationToXLS(deliveryDocumentation);
            filePath = parser.parse();
        }

        return new GenerateResponseForExport().generate(filePath,deliveryDocumentation.getDriverInfo().getFullName(),deliveryDocumentation.getDriverInfo().getPhone());
    }


    @PostMapping("/editDeliveryDocumentation-{userId}-{breedId}")
    public String editDeliveryDocumentation(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,DeliveryDocumentation documentation){
        deliveryDocumentationService.editDeliveryDoc(documentation);
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }


    @PostMapping("/editPackageProduct-{userId}-{breedId}")
    public String editPackageProduct(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, PackagedProduct product){
        packagedProductService.editPackageProduct(product);
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }

    @PostMapping("/addPackageProduct-{userId}-{breedId}")
    public String addPackageProduct(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,PackagedProduct product,
                                    String docId){
        deliveryDocumentationService.addPackageProductToDeliveryDoc(docId,product);
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }

    @PostMapping("/deletePackageProduct-{userId}-{breedId}")
    public String deletePackageProduct(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    String id,String deliveryId){
        deliveryDocumentationService.deletePackage(id,deliveryId);
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }

    //Recycle Page
    @GetMapping("/getListOfRecycle-{userId}-{breedId}")
    public String getListOfRecycle(@PathVariable("userId")int userId,
                                       @PathVariable("breedId")int breedId, Model model){
        if(breedId==2){
            model.addAttribute("fragmentPathTabRecycle","recyclePageOak");
        }else {
            model.addAttribute("fragmentPathTabRecycle","recyclePage");
        }
        model.addAttribute("tabName","recycle");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("contrAgentList",contrAgentService.findAll());
        model.addAttribute("treeStorageList",treeStorageService.getListByUserByBreed(breedId,userId, StatusOfTreeStorage.RECYCLING));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return "fabricPage";
    }

    @PostMapping("/addRecycleRow-{userId}-{breedId}")
    public String  addRecycleRow(@PathVariable("userId")int userId,
                                     @PathVariable("breedId")int breedId,
                                     String nameOfAgent, TreeStorage treeStorage){
        BreedOfTree breedOfTree = new BreedOfTree();
        breedOfTree.setId(breedId);
        treeStorage.setBreedOfTree(breedOfTree);
        UserCompany company = new UserCompany();
        company.setId(userId);
        treeStorage.setUserCompany(company);
        ContrAgent contrAgent =  new ContrAgent();
        contrAgent.setNameOfAgent(nameOfAgent);
        treeStorage.setContrAgent(contrAgent);
        treeStorage.setStatusOfTreeStorage(StatusOfTreeStorage.RECYCLING);
        treeStorageService.putNewTreeStorageObj(treeStorage);
        return "redirect:/fabric/getListOfRecycle-"+userId+"-"+breedId;
    }


    @PostMapping("/cutOfRecycle-{userId}-{breedId}")
    public String  cutOfRecycle(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                          int idOfTreeStorageRow, String extentOfTreeStorage, RawStorage rawStorage){
        TreeStorage treeStorage = treeStorageService.findById(idOfTreeStorageRow);

        treeStorage.setExtent(String.format("%.3f",Float.parseFloat(extentOfTreeStorage)).replace(',','.'));
        rawStorage.setTreeStorage(treeStorage);
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setBreedDescription(treeStorage.getBreedDescription());
        rawStorageService.save(rawStorage);
        return "redirect:/fabric/getListOfRecycle-"+userId+"-"+breedId;
    }

    @PostMapping("/editRecycleRow-{userId}-{breedId}")
    public String editRecycleRow(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                     String nameOfAgent, TreeStorage treeStorage){
        treeStorage.setStatusOfTreeStorage(StatusOfTreeStorage.RECYCLING);
        treeStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        treeStorage.setUserCompany(userCompanyService.findById(userId));
        ContrAgent contrAgent =  new ContrAgent();
        contrAgent.setNameOfAgent(nameOfAgent);
        treeStorage.setContrAgent(contrAgent);
        treeStorageService.putNewTreeStorageObj(treeStorage);
        return "redirect:/fabric/getListOfRecycle-"+userId+"-"+breedId;
    }

    @PostMapping("/exportRecycleXLS-{userId}-{breedId}")
    public ResponseEntity<Resource> exportRecycleXLS(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, String startDate,
                                                         String endDate) throws FileNotFoundException, ParseException {
        ParseTreeStorageToXLS parser = new ParseTreeStorageToXLS(treeStorageService.getListByUserByBreed(breedId,userId, StatusOfTreeStorage.RECYCLING));
        String filePath = parser.parse(startDate,endDate);

        return new GenerateResponseForExport().generate(filePath,startDate,endDate);
    }
}
