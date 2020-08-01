package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.entity.subObjects.DriverInfo;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.*;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import com.swida.documetation.utils.xlsParsers.ParseTreeStorageToXLS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public FabricOakController(TreeStorageService treeStorageService, RawStorageService rawStorageService, DryingStorageService dryingStorageService, DryStorageService dryStorageService, PackagedProductService packagedProductService, DeliveryDocumentationService deliveryDocumentationService, BreedOfTreeService breedOfTreeService, ContrAgentService contrAgentService, UserCompanyService userCompanyService) {
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


    //Tree Storage page
    @GetMapping("/getListOfTreeStorage-{userId}-2")
    public String getListOfTreeStorage(@PathVariable("userId")int userId, Model model){
        int breedId = 2;
        model.addAttribute("fragmentPathTabTreeStorage","treeStorageOAK");
        model.addAttribute("tabName","treeStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("treeStorageList",treeStorageService.getListByUserByBreed(breedId,userId));
        model.addAttribute("contrAgentList",contrAgentService.findAll());
        return "fabricPage";
    }




    @PostMapping("/cutOfTreeStorage-{userId}-2")
    public String  addCutTreeToRawStorage(@PathVariable("userId")int userId, int idOfTreeStorageRow,
                                         RawStorage rawStorage){
        int breedId = 2;

        TreeStorage treeStorage = treeStorageService.findById(idOfTreeStorageRow);

        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setTreeStorage(treeStorage);
        rawStorageService.save(rawStorage);
        treeStorage.setExtent(String.valueOf(Integer.parseInt(treeStorage.getExtent())-Integer.parseInt(rawStorage.getExtent())));
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
        return "fabricPage";
    }
    @PostMapping("/addDeskToDrying-{userId}-2")
    private String addDeskToDrying(@PathVariable("userId")int userId, String rawStorageId, DryingStorage dryingStorage){
        int breedId = 2;

        dryingStorage.setUserCompany(userCompanyService.findById(userId));
        dryingStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(rawStorageId));
        rawStorage.setCountOfDesk(rawStorage.getCountOfDesk()-dryingStorage.getCountOfDesk());
        dryingStorage.setSizeOfWidth(rawStorage.getSizeOfWidth());
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
        return "fabricPage";
    }

    @PostMapping("/addDeskToDryStorage-{userId}-2")
    private String addDeskToDryStorage(@PathVariable("userId")int userId, String dryingStorageId, String codeOfProduct){
        int breedId = 2;

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

    @PostMapping("/editDryingStorageObj-{userId}-2")
    public String editDryingStorageObj(@PathVariable("userId")int userId, DryingStorage dryingStorage ){
        int breedId = 2;

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
        return "fabricPage";
    }

    @PostMapping("/editDryStorageRow-{userId}-2")
    public String editDryStorageRow(@PathVariable("userId")int userId, String id, String codeOfProduct){
        int breedId = 2;
        DryStorage dryStorage = dryStorageService.findById(Integer.parseInt(id));
        dryStorage.setCodeOfProduct(codeOfProduct);
        dryStorageService.save(dryStorage);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/createPackages-{userId}-2")
    public String createPackages(@PathVariable("userId")int userId, String id, String codeOfProduct,String height, String width, String count, String longFact){
        int breedId = 2;
        packagedProductService.createPackages(id,codeOfProduct,height,width,count,longFact,userCompanyService.findById(userId));

        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    //Packaged product page
    @GetMapping("/getListOfPackagedProduct-{userId}-2")
    public String getListOfPackagedProduct(@PathVariable("userId")int userId, Model model){
        int breedId = 2;
        model.addAttribute("fragmentPathTabPackageStorage","packageStorage");
        model.addAttribute("tabName","packageStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("packagedProductsList",packagedProductService.getListByUserByBreed(breedId,userId, StatusOfProduct.ON_STORAGE));
        return "fabricPage";
    }




}

