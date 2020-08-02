package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.entity.subObjects.DriverInfo;
import com.swida.documetation.data.enums.Roles;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.*;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import com.swida.documetation.utils.other.GenerateResponseForExport;
import com.swida.documetation.utils.xlsParsers.*;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.File;
import java.io.FileInputStream;
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

    @Autowired
    public FabricController(TreeStorageService treeStorageService, RawStorageService rawStorageService, DryingStorageService dryingStorageService, DryStorageService dryStorageService, PackagedProductService packagedProductService, DeliveryDocumentationService deliveryDocumentationService, BreedOfTreeService breedOfTreeService, ContrAgentService contrAgentService, UserCompanyService userCompanyService) {
        this.treeStorageService = treeStorageService;
        this.rawStorageService = rawStorageService;
        this.dryingStorageService = dryingStorageService;
        this.dryStorageService = dryStorageService;
        this.packagedProductService = packagedProductService;
        this.deliveryDocumentationService = deliveryDocumentationService;
        this.breedOfTreeService = breedOfTreeService;
        this.contrAgentService = contrAgentService;
        this.userCompanyService = userCompanyService;
    }

    @GetMapping("/index-{userId}")
    public String index(@PathVariable("userId")int userId){
//        UserCompany company  = new UserCompany();
//        company.setRole(Roles.ROLES_USER);
//        company.setUsername("9");
//        company.setPassword("9");
//        company.setNameOfCompany("SuperPylka");
//        userCompanyService.save(company);
        int breedId = 1;
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
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
        model.addAttribute("treeStorageList",treeStorageService.getListByUserByBreed(breedId,userId));
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
        treeStorageService.putNewTreeStorageObj(treeStorage);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }


    @PostMapping("/cutOfTreeStorage-{userId}-{breedId}")
    public String  addCutTreeToRawStorage(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                       int idOfTreeStorageRow, String extentOfTreeStorage, RawStorage rawStorage){
        TreeStorage treeStorage = treeStorageService.findById(idOfTreeStorageRow);
        treeStorage.setExtent(extentOfTreeStorage);
        rawStorage.setTreeStorage(treeStorage);
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setBreedDescription(treeStorage.getBreedDescription());
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
        treeStorageService.putNewTreeStorageObj(treeStorage);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/exportTreeStorageXLS-{userId}-{breedId}")
    public ResponseEntity<Resource> exportTreeStorageXLS(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, String startDate,
                                                         String endDate) throws FileNotFoundException, ParseException {
        ParseTreeStorageToXLS parser = new ParseTreeStorageToXLS(treeStorageService.getListByUserByBreed(breedId,userId));
        String filePath = parser.parse(startDate,endDate);

        return new GenerateResponseForExport().generate(filePath,startDate,endDate);
    }


    @PostMapping("/addDeskFromProvider-{userId}-{breedId}")
    public String addDeskFromProvider(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                      TreeStorage treeStorage, String sizeOfHeight, String sizeOfWidth,
                                      String sizeOfLong, String countOfDesk, String nameOfAgent, String extent){
        treeStorage.setUserCompany(userCompanyService.findById(userId));
        treeStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        treeStorage.setExtent(extent);
        ContrAgent contrAgent =  new ContrAgent();
        contrAgent.setNameOfAgent(nameOfAgent);
        treeStorage.setContrAgent(contrAgent);
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
        dryingStorage.setBreedDescription(rawStorage.getBreedDescription());
        rawStorageService.save(rawStorage);
        dryingStorage.setRawStorage(rawStorage);
        dryingStorageService.save(dryingStorage);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editRawStorageObj-{userId}-{breedId}")
    public String editRawStorageObj(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    RawStorage rawStorage){

        RawStorage rawStorageDB = rawStorageService.findById(rawStorage.getId());
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


    //Drying page
    @GetMapping("/getListOfDryingStorage-{userId}-{breedId}")
    public String getListOfDryingStorage(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,Model model){
        model.addAttribute("fragmentPathTabDryingStorage","dryingStorage");
        model.addAttribute("tabName","dryingStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("dryingStorageList",dryingStorageService.getListByUserByBreed(breedId,userId));
        return "fabricPage";
    }

    @PostMapping("/addDeskToDryStorage-{userId}-{breedId}")
    private String addDeskToDryStorage(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                   String dryingStorageId, String codeOfProduct){

        DryingStorage dryingStorageDB = dryingStorageService.findById(Integer.parseInt(dryingStorageId));
        DryStorage dryStorage = new DryStorage();
        dryStorage.setCodeOfProduct(codeOfProduct);

        dryStorage.setBreedOfTree(dryingStorageDB.getBreedOfTree());
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
        return "fabricPage";
    }

    @PostMapping("/editDryStorageRow-{userId}-{breedId}")
    public String editDryStorageRow(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,String id,
                                  String codeOfProduct){
        DryStorage dryStorage = dryStorageService.findById(Integer.parseInt(id));
        dryStorage.setCodeOfProduct(codeOfProduct);
        dryStorageService.save(dryStorage);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/createPackages-{userId}-{breedId}")
    public String createPackages(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,String id,
                                    String codeOfProduct,String height, String width, String count, String longFact, String heightWidth){
        packagedProductService.createPackages(id,codeOfProduct,height,width,count,longFact,heightWidth,userCompanyService.findById(userId));


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
        model.addAttribute("fragmentPathTabPackageStorage","packageStorage");
        model.addAttribute("tabName","packageStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("packagedProductsList",packagedProductService.getListByUserByBreed(breedId,userId, StatusOfProduct.ON_STORAGE));
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

        model.addAttribute("fragmentPathTabDelivery","deliveryInfo");
        model.addAttribute("tabName","deliveryInfo");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("deliveryDocumentations",deliveryDocumentationService.findAll());
        return "fabricPage";
    }

    @PostMapping("/exportDeliveryInfo-{id}")
    public ResponseEntity<Resource> exportDeliveryInfo(@PathVariable("id") int id) throws FileNotFoundException {
        DeliveryDocumentation deliveryDocumentation = deliveryDocumentationService.findById(id);
        ParserDeliveryDocumentationToXLS parser = new ParserDeliveryDocumentationToXLS(deliveryDocumentation);
        String filePath = parser.parse();

        return new GenerateResponseForExport().generate(filePath,deliveryDocumentation.getDriverInfo().getFullName(),deliveryDocumentation.getDriverInfo().getPhone());
    }

}
