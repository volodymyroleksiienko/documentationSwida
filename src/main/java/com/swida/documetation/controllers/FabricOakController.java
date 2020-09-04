package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.entity.subObjects.DriverInfo;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.*;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import com.swida.documetation.utils.xlsParsers.ParseTreeStorageToXLS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("fabric")
@Controller
public class FabricOakController {
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
    private DescriptionDeskOakService deskOakService;

    @Autowired
    public FabricOakController(TreeStorageService treeStorageService, RawStorageService rawStorageService,
                               DryingStorageService dryingStorageService, DryStorageService dryStorageService,
                               PackagedProductService packagedProductService, DeliveryDocumentationService deliveryDocumentationService,
                               BreedOfTreeService breedOfTreeService, ContrAgentService contrAgentService,
                               UserCompanyService userCompanyService, OrderInfoService orderInfoService,
                               DescriptionDeskOakService deskOakService) {
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
        this.deskOakService = deskOakService;
    }
    private void btnConfig(int userId, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        if(hasAdminRole || userId==userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId()){
            model.addAttribute("btnConfig","btnON");
        }

    }
    //Tree Storage page
    @GetMapping("/getListOfTreeStorage-{userId}-2")
    public String getListOfTreeStorage(@PathVariable("userId")int userId, Model model){
        int breedId = 2;
        model.addAttribute("fragmentPathTabTreeStorage","treeStorageOAK");
        model.addAttribute("tabName","treeStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("treeStorageList",treeStorageService.getListByUserByBreed(breedId,userId, StatusOfTreeStorage.TREE));
        model.addAttribute("contrAgentList",contrAgentService.findAll());
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        btnConfig(userId,model);
        return "fabricPage";
    }




    @PostMapping("/cutOfTreeStorage-{userId}-2")
    public String  addCutTreeToRawStorage(@PathVariable("userId")int userId, int idOfTreeStorageRow,
                                         RawStorage rawStorage, String usedExtent,String extentOfWaste){
        int breedId = 2;

        TreeStorage treeStorage = treeStorageService.findById(idOfTreeStorageRow);

        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setTreeStorage(treeStorage);
        rawStorageService.save(rawStorage);
        treeStorage.setExtent(String.format("%.3f",Float.parseFloat(treeStorage.getExtent())-Float.parseFloat(usedExtent)).replace(',','.'));

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

        treeStorageService.save(treeStorage);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editTreeStorageRow-{userId}-2")
    public String editTreeStorageRow(@PathVariable("userId")int userId, String nameOfAgent, TreeStorage treeStorage){
        int breedId = 2;

        treeStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        treeStorage.setUserCompany(userCompanyService.findById(userId));
        ContrAgent contrAgent =  new ContrAgent();
        contrAgent.setNameOfAgent(nameOfAgent);
        treeStorage.setContrAgent(contrAgent);
        treeStorageService.putNewTreeStorageObj(treeStorage);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/addDeskFromProvider-{userId}-2")
    public String addDeskFromProvider(@PathVariable("userId")int userId, TreeStorage treeStorage, String sizeOfHeight,
                                      String description, String nameOfAgent, String extent){
       int breedId = 2;

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
        rawStorage.setExtent(extent);

        rawStorage.setSizeOfHeight(sizeOfHeight);
        rawStorage.setDescription(description);

        rawStorageService.save(rawStorage);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }


    //RawStorage page
    @GetMapping("/getListOfRawStorage-{userId}-2")
    public String getListOfRawStorage(@PathVariable("userId")int userId, Model model){
        int breedId = 2;

        model.addAttribute("fragmentPathTabRawStorage","rawStorageOAK");
        model.addAttribute("tabName","rawStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("rawStorageList",rawStorageService.getListByUserByBreed(breedId, userId));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        btnConfig(userId,model);
        return "fabricPage";
    }
    @PostMapping("/addDeskToDrying-{userId}-2")
    private String addDeskToDrying(@PathVariable("userId")int userId, String rawStorageId, DryingStorage dryingStorage,
                                   String extentOfDrying){
        int breedId = 2;

        dryingStorage.setUserCompany(userCompanyService.findById(userId));
        dryingStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        dryingStorage.setExtent(extentOfDrying);

        RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(rawStorageId));
        rawStorage.setExtent(String.format("%.3f",Float.parseFloat(rawStorage.getExtent())-Float.parseFloat(extentOfDrying)).replace(',','.'));

        dryingStorage.setSizeOfHeight(rawStorage.getSizeOfHeight());
        dryingStorage.setBreedDescription(rawStorage.getBreedDescription());
        rawStorageService.save(rawStorage);
        dryingStorage.setRawStorage(rawStorage);
        dryingStorageService.save(dryingStorage);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editRawStorageObj-{userId}-2")
    public String editRawStorageObj(@PathVariable("userId")int userId, RawStorage rawStorage){
        int breedId = 2;

        RawStorage rawStorageDB = rawStorageService.findById(rawStorage.getId());
        rawStorageDB.setBreedDescription(rawStorage.getBreedDescription());
        rawStorageDB.setCodeOfProduct(rawStorage.getCodeOfProduct());
        rawStorageDB.setExtent(rawStorage.getExtent());
        rawStorageDB.setSizeOfHeight(rawStorage.getSizeOfHeight());
        rawStorageDB.setDescription(rawStorage.getDescription());
        rawStorageService.save(rawStorageDB);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }


    //Drying page
    @GetMapping("/getListOfDryingStorage-{userId}-2")
    public String getListOfDryingStorage(@PathVariable("userId")int userId, Model model){
        int breedId = 2;

        model.addAttribute("fragmentPathTabDryingStorage","dryingStorageOAK");
        model.addAttribute("tabName","dryingStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("dryingStorageList",dryingStorageService.getListByUserByBreed(breedId,userId));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        btnConfig(userId,model);
        return "fabricPage";
    }

    @PostMapping("/addDeskToDryStorage-{userId}-2")
    private String addDeskToDryStorage(@PathVariable("userId")int userId, String dryingStorageId, String codeOfProduct,
                                       String breedDescription){
        int breedId = 2;

        DryingStorage dryingStorageDB = dryingStorageService.findById(Integer.parseInt(dryingStorageId));
        DryStorage dryStorage = new DryStorage();
        dryStorage.setCodeOfProduct(codeOfProduct);

        dryStorage.setBreedOfTree(dryingStorageDB.getBreedOfTree());
        dryStorage.setBreedDescription(breedDescription);
        dryStorage.setExtent(dryingStorageDB.getExtent());
        dryStorage.setSizeOfHeight(dryingStorageDB.getSizeOfHeight());
        dryStorage.setDescription(dryingStorageDB.getDescription());
        dryStorage.setUserCompany(dryingStorageDB.getUserCompany());
        dryStorage.setDryingStorage(dryingStorageDB);

        dryStorageService.save(dryStorage);
        dryingStorageDB.setExtent("0.000");
        dryingStorageService.save(dryingStorageDB);
        return "redirect:/fabric/getListOfDryingStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editDryingStorageObj-{userId}-2")
    public String editDryingStorageObj(@PathVariable("userId")int userId, DryingStorage dryingStorage,String breedDescription,
                                       String description){
        int breedId = 2;

        DryingStorage dryingStorageDB = dryingStorageService.findById(dryingStorage.getId());
        if(dryingStorageDB.getCountOfDesk()!=dryingStorage.getCountOfDesk()){
            int count = dryingStorageDB.getCountOfDesk()-dryingStorage.getCountOfDesk();
            RawStorage rawStorage = dryingStorageDB.getRawStorage();
            rawStorage.setCountOfDesk(rawStorage.getCountOfDesk()+count);
            rawStorageService.save(rawStorage);
        }
        dryingStorageDB.setDescription(description);
        dryingStorageDB.setCodeOfProduct(dryingStorage.getCodeOfProduct());
        dryingStorageDB.setCountOfDesk(dryingStorage.getCountOfDesk());
        dryingStorageDB.setBreedDescription(breedDescription);
        dryingStorageService.save(dryingStorageDB);
        return "redirect:/fabric/getListOfDryingStorage-"+userId+"-"+breedId;
    }


    //Dry Storage page
    @GetMapping("/getListOfDryStorage-{userId}-2")
    public String getListOfDryStorage(@PathVariable("userId")int userId,Model model){
        int breedId = 2;

        model.addAttribute("fragmentPathTabDryStorage","dryStorageOAK");
        model.addAttribute("tabName","dryStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("dryStorageList",dryStorageService.getListByUserByBreed(breedId,userId));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        btnConfig(userId,model);
        return "fabricPage";
    }

    @PostMapping("/editDryStorageRow-{userId}-2")
    public String editDryStorageRow(@PathVariable("userId")int userId, String id, String codeOfProduct,String breedDescription){
        int breedId = 2;
        DryStorage dryStorage = dryStorageService.findById(Integer.parseInt(id));
        dryStorage.setCodeOfProduct(codeOfProduct);
        dryStorage.setBreedDescription(breedDescription);
        dryStorageService.save(dryStorage);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }


    //Packaged product page
    @GetMapping("/getListOfPackagedProduct-{userId}-2")
    public String getListOfPackagedProduct(@PathVariable("userId")int userId, Model model){
        int breedId = 2;
        model.addAttribute("fragmentPathTabPackageStorage","packageStorageOak");
        model.addAttribute("tabName","packageStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("packageOakList",packagedProductService.getListByUserByBreed(breedId,userId, StatusOfProduct.ON_STORAGE));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("deliveryList",deliveryDocumentationService.getListByUserByBreed(breedId,userId));

        UserCompany company = userCompanyService.findById(userId);
        ContrAgent contrAgent = company.getContrAgent();
        model.addAttribute("contractList",orderInfoService.getOrdersListByAgentByBreed(contrAgent.getId(),breedId));
        btnConfig(userId,model);

        return "fabricPage";
    }


    //Delivery page
    @GetMapping("/getListOfDeliveryDocumentation-{userId}-2")
    public String getListOfDeliveryDocumentation(@PathVariable("userId")int userId,Model model){

        int breedId = 2;
        UserCompany company = userCompanyService.findById(userId);
        ContrAgent contrAgent = company.getContrAgent();

        model.addAttribute("fragmentPathTabDelivery","deliveryInfoOak");
        model.addAttribute("tabName","deliveryInfo");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("deliveryDocumentations",deliveryDocumentationService.getListByUserByBreed(breedId,userId));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("contractList",orderInfoService.getOrdersListByAgentByBreed(contrAgent.getId(),breedId));


        model.addAttribute("urlEditDriver","/fabric/editDeliveryDocumentation-"+userId+"-"+breedId);
        model.addAttribute("urlEditPackage","/fabric/editPackageProduct-"+userId+"-"+breedId);
        model.addAttribute("urlAddPackage","/fabric/addPackageProduct-"+userId+"-"+breedId);
        model.addAttribute("urlDeletePackage","/fabric/deletePackageProduct-"+userId+"-"+breedId);
        model.addAttribute("urlEditPackageDescriptionOak","/fabric/editPackageDescriptionOak-"+userId+"-"+breedId);
        model.addAttribute("urlAddPackageDescriptionOak","/fabric/addPackageDescriptionOak-"+userId+"-"+breedId);
        model.addAttribute("urlDeleteDescriptionOak","/fabric/deletePackageDescriptionOak-"+userId+"-"+breedId);
        btnConfig(userId,model);
        return "fabricPage";
    }

    public void reloadAllExtentFields(DeliveryDocumentation deliveryDocumentation ){
        deliveryDocumentationService.reloadExtentOfAllPack(deliveryDocumentation);
        List<Integer> list = new ArrayList<>();
        list.add(deliveryDocumentation.getOrderInfo().getId());
        List<DeliveryDocumentation> docList = deliveryDocumentationService.getListByDistributionContractsId(list);
        orderInfoService.reloadOrderExtent(deliveryDocumentation.getOrderInfo(),docList);
        orderInfoService.reloadMainOrderExtent(deliveryDocumentation.getOrderInfo().getMainOrder());
    }

    @PostMapping("/editPackageDescriptionOak-{userId}-{breedId}")
    public String editPackageDescriptionOak(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,
                                            String rowId,String packageId, String width, String count) {

        deskOakService.editDescription(rowId,width,count);
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(packageId));
        packagedProductService.countExtentOak(product);
        reloadAllExtentFields(product.getDeliveryDocumentation());
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }

    @PostMapping("/addPackageDescriptionOak-{userId}-{breedId}")
    public String addPackageDescriptionOak(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,
                                            String packageId, String width, String count) {
        packagedProductService.addDescriptionOak(packageId,width,count);
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(packageId));
        packagedProductService.countExtentOak(product);
        reloadAllExtentFields(product.getDeliveryDocumentation());
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }

    @PostMapping("/deletePackageDescriptionOak-{userId}-{breedId}")
    public String deletePackageDescriptionOak(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,
                                           String packageId, String id) {
        packagedProductService.deleteDescriptionOak(packageId,id);
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(packageId));
        packagedProductService.countExtentOak(product);
        reloadAllExtentFields(product.getDeliveryDocumentation());
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }




//    Recycle page
    @PostMapping("/cutOfRecycle-{userId}-2")
    public String  cutOfRecycle(@PathVariable("userId")int userId,
                                int idOfTreeStorageRow, String extentOfTreeStorage, RawStorage rawStorage){
        int breedId =2;
        TreeStorage treeStorage = treeStorageService.findById(idOfTreeStorageRow);

        treeStorage.setExtent(String.format("%.3f",Float.parseFloat(extentOfTreeStorage)).replace(',','.'));
        rawStorage.setTreeStorage(treeStorage);
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setBreedDescription(treeStorage.getBreedDescription());
        rawStorageService.save(rawStorage);
        return "redirect:/fabric/getListOfRecycle-"+userId+"-"+breedId;
    }
}

