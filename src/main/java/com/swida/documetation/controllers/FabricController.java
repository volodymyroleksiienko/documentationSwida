package com.swida.documetation.controllers;

import com.google.gson.Gson;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.ContrAgentType;
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
import com.swida.documetation.utils.other.PackageProductToJson;
import com.swida.documetation.utils.xlsParsers.*;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    private void btnConfig(int userId, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        if(hasAdminRole || userId==userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId()){
            model.addAttribute("btnConfig","btnON");
        }

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
        model.addAttribute("orderInfoList",orderInfoService.getOrdersListByAgentByBreed(contrAgent.getId(),breedId));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());

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
        model.addAttribute("orderList",orderInfoService.getOrdersListByBreed(breedId));
        model.addAttribute("contrAgentList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("treeStorageList",treeStorageService.getListByUserByBreed(breedId,userId, StatusOfTreeStorage.TREE));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        btnConfig(userId,model);

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
        treeStorage.setExtent(String.format("%.3f", Float.parseFloat(treeStorage.getExtent())).replace(',', '.'));
        treeStorage.setMaxExtent(treeStorage.getExtent());
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
        treeStorage.setRecycle(recycle);
        treeStorageService.save(treeStorage);
        rawStorageService.save(rawStorage);
        treeStorageService.checkQualityInfo(treeStorage,rawStorage.getSizeOfHeight(),Float.parseFloat(rawStorage.getExtent()));
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editTreeStorageRow-{userId}-{breedId}")
    public String editTreeStorageRow(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                     String nameOfAgent, TreeStorage treeStorage){
        treeStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        treeStorage.setUserCompany(userCompanyService.findById(userId));
//        ContrAgent contrAgent =  new ContrAgent();
//        contrAgent.setNameOfAgent(nameOfAgent);
//        treeStorage.setContrAgent(contrAgent);
        treeStorage.setExtent(String.format("%.3f", Float.parseFloat(treeStorage.getExtent())).replace(',', '.'));
//        treeStorage.setMaxExtent(treeStorage.getExtent());
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

    @PostMapping("/exportHistoryTreeStorageXLS-{userId}-{breedId}")
    public ResponseEntity<Resource> exportHistoryTreeStorageXLS(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, String startDate,
                                                         String endDate) throws FileNotFoundException, ParseException {
        ParseTreeStorageToXLS parser = new ParseTreeStorageToXLS(treeStorageService.getListByUserByBreedALL(breedId,userId, StatusOfTreeStorage.TREE));
        String filePath = parser.parse(startDate,endDate);

        return new GenerateResponseForExport().generate(filePath,startDate,endDate);
    }

    @PostMapping("/exportProviderHistoryTreeStorageXLS-{userId}-{breedId}")
    public ResponseEntity<Resource> exportProviderHistoryTreeStorageXLS(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, String startDate,
                                                         String endDate) throws FileNotFoundException, ParseException {
        String filePath;
        if(breedId==2) {
            ParseTreeStorageToXLS parser = new ParseTreeStorageToXLS(treeStorageService.getListByUserByBreedALL(breedId, userId, StatusOfTreeStorage.PROVIDER_DESK));
            filePath = parser.parse(startDate, endDate);
        }else{
            ParseRawStorageToXLS parser = new ParseRawStorageToXLS(rawStorageService.getListByUserByBreedByStatusOfTree(breedId, userId, StatusOfTreeStorage.PROVIDER_DESK));
            filePath = parser.parse(startDate, endDate);
        }

        return new GenerateResponseForExport().generate(filePath,startDate,endDate);
    }


    @ResponseBody
    @PostMapping("/addDeskFromProvider-{userId}-{breedId}")
    public JSONObject addDeskFromProvider(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                      String codeOfProduct,String breedDescription, String sizeOfHeight, String sizeOfWidth,
                                      String sizeOfLong, String countOfDesk, String contrAgentId, String extent, String contractId) throws net.minidev.json.parser.ParseException {
        TreeStorage treeStorage = new TreeStorage();
        treeStorage.setCodeOfProduct(codeOfProduct);
        treeStorage.setBreedDescription(breedDescription);
        treeStorage.setContrAgent(contrAgentService.findById(Integer.parseInt(contrAgentId)));
        treeStorage.setUserCompany(userCompanyService.findById(userId));
        treeStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        treeStorage.setExtent("0.000");

        treeStorage.setStatusOfTreeStorage(StatusOfTreeStorage.PROVIDER_DESK);
        treeStorageService.save(treeStorage);

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
        rawStorage.setMaxCountOfDesk(rawStorage.getCountOfDesk());

        String rawExtent =  rawStorageService.save(rawStorage);


        treeStorage.setMaxExtent(rawExtent);
        treeStorageService.save(treeStorage);


        JSONObject json = new JSONObject();
        json.put("rawStorageId",rawStorage.getId());
        json.put("treeStorageExtent",treeStorage.getExtent());
        json.put("rawStorageExtent",rawStorage.getExtent());
        return json;
//        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
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
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        model.addAttribute("contrAgentList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        btnConfig(userId,model);
        return "fabricPage";
    }


    @PostMapping("/addDeskToDrying-{userId}-{breedId}")
    private String addDeskToDrying(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    String rawStorageId, DryingStorage dryingStorage, String date){
        dryingStorage.setUserCompany(userCompanyService.findById(userId));
        dryingStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(rawStorageId));
        rawStorage.setCountOfDesk(rawStorage.getCountOfDesk()-dryingStorage.getCountOfDesk());
        dryingStorage.setSizeOfWidth(rawStorage.getSizeOfWidth());
        dryingStorage.setSizeOfHeight(rawStorage.getSizeOfHeight());
        dryingStorage.setSizeOfLong(rawStorage.getSizeOfLong());
        dryingStorage.setDateDrying(date);
        rawStorageService.save(rawStorage);
        dryingStorage.setRawStorage(rawStorage);
        dryingStorageService.save(dryingStorage);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editRawStorageObj-{userId}-{breedId}")
    public String editRawStorageObj(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    RawStorage rawStorage){

        RawStorage rawStorageDB = rawStorageService.findById(rawStorage.getId());
        float oldExtent = Float.parseFloat(rawStorageDB.getExtent());
        rawStorageDB.setBreedDescription(rawStorage.getBreedDescription());
        rawStorageDB.setCodeOfProduct(rawStorage.getCodeOfProduct());
        rawStorageDB.setCountOfDesk(rawStorage.getCountOfDesk());
        rawStorageDB.setSizeOfHeight(rawStorage.getSizeOfHeight());
        rawStorageDB.setSizeOfLong(rawStorage.getSizeOfLong());
        rawStorageDB.setSizeOfWidth(rawStorage.getSizeOfWidth());
        rawStorageService.save(rawStorageDB);

        TreeStorage treeStorage = rawStorageDB.getTreeStorage();

        treeStorage.setExtent(
                String.format("%.3f",Float.parseFloat(treeStorage.getExtent())-(Float.parseFloat(rawStorageService.findById(rawStorageDB.getId()).getExtent())-oldExtent)).replace(",",".")
        );
        if(treeStorage.getStatusOfTreeStorage()==StatusOfTreeStorage.PROVIDER_DESK){
            treeStorage.setMaxExtent(
                    String.format("%.3f",Float.parseFloat(treeStorage.getMaxExtent())+(Float.parseFloat(rawStorageService.findById(rawStorageDB.getId()).getExtent())-oldExtent)).replace(",",".")
            );
            treeStorage.setExtent("0.000");
            rawStorageDB.setMaxCountOfDesk(rawStorage.getCountOfDesk());
            rawStorageService.save(rawStorageDB);
        }
        treeStorageService.save(treeStorage);
        if (treeStorage.getStatusOfTreeStorage()==StatusOfTreeStorage.PROVIDER_DESK && treeStorage.getOrderInfo()!=null){
            OrderInfo orderInfo = treeStorage.getOrderInfo();
            orderInfo.setDoneExtendOfOrder(
                    String.format("%.3f",
                            Float.parseFloat(orderInfo.getDoneExtendOfOrder())-(Float.parseFloat(rawStorageService.findById(rawStorageDB.getId()).getExtent())-oldExtent))
                            .replace(",",".")
            );
            orderInfoService.save(orderInfo);
            orderInfoService.reloadMainOrderExtent(orderInfo.getMainOrder());
        }
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/returnRawPackageToTree-{userId}-{breedId}")
    public String returnRawPackageToTree(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    String id){
        RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(id));
        TreeStorage treeStorage = rawStorage.getTreeStorage();
        float rawStorageExtent = Float.parseFloat(rawStorage.getExtent());
        float recycleExtent=0;

        if (treeStorage.getStatusOfTreeStorage()==StatusOfTreeStorage.PROVIDER_DESK && treeStorage.getOrderInfo()!=null){
            OrderInfo orderInfo = treeStorage.getOrderInfo();
            orderInfo.setDoneExtendOfOrder(
                    String.format("%.3f",
                            Float.parseFloat(orderInfo.getDoneExtendOfOrder())-Float.parseFloat(rawStorage.getExtent()))
                            .replace(",",".")
            );
            orderInfoService.save(orderInfo);
            orderInfoService.reloadMainOrderExtent(orderInfo.getMainOrder());
        }
        if(treeStorage.getRecycle()!=null){
            recycleExtent=Float.parseFloat(treeStorage.getRecycle().getExtent());
            treeStorage.getRecycle().setExtent("0.000");
            treeStorageService.save(treeStorage.getRecycle());
        }
        treeStorage.setExtent(
                String.format("%.3f",Float.parseFloat(treeStorage.getExtent())
                +Float.parseFloat(rawStorage.getExtent())+recycleExtent).replace(",",".")
        );
        rawStorage.setExtent("0.000");
        rawStorage.setCountOfDesk(0);
        rawStorageService.save(rawStorage);
        treeStorageService.save(treeStorage);
        treeStorageService.checkQualityInfo(treeStorage,rawStorage.getSizeOfHeight(),-rawStorageExtent);
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
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        btnConfig(userId,model);
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

//        DryingStorage dryingStorageDB = dryingStorageService.findById(dryingStorage.getId());
//        if(dryingStorageDB.getCountOfDesk()!=dryingStorage.getCountOfDesk()){
//            int count = dryingStorageDB.getCountOfDesk()-dryingStorage.getCountOfDesk();
//            RawStorage rawStorage = dryingStorageDB.getRawStorage();
//            rawStorage.setCountOfDesk(rawStorage.getCountOfDesk()+count);
//            rawStorageService.save(rawStorage);
//        }
//        dryingStorageDB.setBreedDescription(dryingStorage.getBreedDescription());
//        dryingStorageDB.setCodeOfProduct(dryingStorage.getCodeOfProduct());
//        dryingStorageDB.setCountOfDesk(dryingStorage.getCountOfDesk());
//        dryingStorageService.save(dryingStorageDB);

        dryingStorageService.editDryingStorage(dryingStorage);

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
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        btnConfig(userId,model);
        return "fabricPage";
    }

    @PostMapping("/editDryStorageRow-{userId}-{breedId}")
    public String editDryStorageRow(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,DryStorage dryStorage){

        dryStorageService.editDryStorage(dryStorage);
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

        model.addAttribute("contractList",orderInfoService.getOrdersListByAgentByBreed(contrAgent.getId(),breedId));
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("packagedProductsList",packagedProductService.getListByUserByBreed(breedId,userId, StatusOfProduct.ON_STORAGE));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("deliveryList",deliveryDocumentationService.getListByUserByBreed(breedId,userId));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        btnConfig(userId,model);
        return "fabricPage";
    }

    @PostMapping("/unformPackagedProduct-{userId}-{breedId}")
    public  String unformPackagedProduct(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, String id){

        PackagedProduct product = packagedProductService.findById(Integer.parseInt(id));

        if(product.getDryStorage()==null){
            packagedProductService.deleteByID(product.getId());
            return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+breedId;
        }

        DryStorage dryStorage = product.getDryStorage();

       if(breedId!=2) {
           dryStorage.setCountOfDesk(dryStorage.getCountOfDesk() + Integer.parseInt(product.getCountOfDesk()));
       }else{
           dryStorage.setExtent(String.format("%.3f",Float.parseFloat(dryStorage.getExtent())+Float.parseFloat(product.getExtent())).replace(",","."));
       }
       dryStorageService.save(dryStorage);
       packagedProductService.deleteByID(Integer.parseInt(id));

        return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+breedId;
    }


    @PostMapping("/addPackagedProductWithoutHistory-{userId}-{breedId}")
    public String addPackagedProductWithoutHistory(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                                  PackagedProduct product, String countOfPacks){

        packagedProductService.createPackagesWithoutHistory(product,countOfPacks,breedId,userId);
        return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+breedId;
    }

    @PostMapping("/editPack-{userId}-{breedId}")
    public String editPack(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,PackagedProduct product){
        int oldCountOFDesk = Integer.parseInt(packagedProductService.findById(product.getId()).getCountOfDesk());
        PackagedProduct productDB = packagedProductService.editPackageProduct(product);
        productDB.setExtent(packagedProductService.countExtent(productDB));
        productDB.setCountOfDesk(packagedProductService.countDesk(productDB));
        packagedProductService.save(productDB);
        if (productDB.getDryStorage()!=null){
            DryStorage dryStorage = dryStorageService.findById(productDB.getDryStorage().getId());
            if(breedId!=2) {
                dryStorage.setCountOfDesk(dryStorage.getCountOfDesk() - (Integer.parseInt(productDB.getCountOfDesk())-oldCountOFDesk));
            }
            dryStorageService.save(dryStorage);
        }
        return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+breedId;
    }

    @PostMapping("/addPackToExistDeliveryDoc-{userId}-{breedId}")
    public String addPackToExistDeliveryDoc(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                       String packId,String driverId){
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(packId));
        DeliveryDocumentation deliveryDocumentation = deliveryDocumentationService.findById(Integer.parseInt(driverId));
        product.setStatusOfProduct(StatusOfProduct.IN_DELIVERY);
        product.setDeliveryDocumentation(deliveryDocumentation);
        packagedProductService.save(product);
        if (deliveryDocumentation.getProductList()==null){
            List<PackagedProduct> list = new ArrayList<>();
            list.add(product);
            deliveryDocumentation.setProductList(list);
        }else{
            deliveryDocumentation.getProductList().add(product);
        }
        deliveryDocumentationService.save(deliveryDocumentation);

        //reload order extent info
        reloadAllExtentFields(product.getDeliveryDocumentation());
        deliveryDocumentationService.checkHeightUnicValue(deliveryDocumentation);

        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }

    public void reloadAllExtentFields(DeliveryDocumentation deliveryDocumentation ){
        deliveryDocumentationService.reloadExtentOfAllPack(deliveryDocumentation);
        List<Integer> list = new ArrayList<>();
        list.add(deliveryDocumentation.getOrderInfo().getId());
        List<DeliveryDocumentation> docList = deliveryDocumentationService.getListByDistributionContractsId(list);
        orderInfoService.reloadOrderExtent(deliveryDocumentation.getOrderInfo(),docList);
        orderInfoService.reloadMainOrderExtent(deliveryDocumentation.getOrderInfo().getMainOrder());
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
        model.addAttribute("contractList",orderInfoService.getOrdersListByAgentByBreed(contrAgent.getId(),breedId));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("dryStorageList",dryStorageService.getListByUserByBreed(breedId,userId));

        model.addAttribute("urlEditDriver","/fabric/editDeliveryDocumentation-"+userId+"-"+breedId);
        model.addAttribute("urlEditPackage","/fabric/editPackageProduct-"+userId+"-"+breedId);
        model.addAttribute("urlAddPackage","/fabric/addPackageProduct-"+userId+"-"+breedId);
        model.addAttribute("urlDeletePackage","/fabric/deletePackageProduct-"+userId+"-"+breedId);
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        btnConfig(userId,model);
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
        ResponseEntity<Resource> responseEntity = new GenerateResponseForExport().generate(filePath,deliveryDocumentation.getDriverInfo().getFullName(),deliveryDocumentation.getDriverInfo().getPhone());

        return responseEntity;    }


    @PostMapping("/editDeliveryDocumentation-{userId}-{breedId}")
    public String editDeliveryDocumentation(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,DeliveryDocumentation documentation){
        DeliveryDocumentation deliveryDocumentation = deliveryDocumentationService.editDeliveryDoc(documentation);
        deliveryDocumentationService.checkHeightUnicValue(deliveryDocumentation);
        reloadAllExtentFields(deliveryDocumentation);
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }


    @PostMapping("/editPackageProduct-{userId}-{breedId}")
    public String editPackageProduct(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, PackagedProduct product){
        int oldPackCountDesk = Integer.parseInt(packagedProductService.findById(product.getId()).getCountOfDesk());
        float oldPackExtent = Float.parseFloat(packagedProductService.findById(product.getId()).getExtent());
        PackagedProduct packagedProduct = packagedProductService.editPackageProduct(product);
        if (packagedProduct.getDryStorage()!=null){
            DryStorage dryStorage = dryStorageService.findById(packagedProduct.getDryStorage().getId());
            if(breedId!=2){
                 int oldDesk = dryStorage.getCountOfDesk();
                 dryStorage.setCountOfDesk(oldDesk-(Integer.parseInt(packagedProduct.getCountOfDesk())-oldPackCountDesk));
            }else {
                float oldExtent = Float.parseFloat(dryStorage.getExtent());
                packagedProductService.countExtentOak(packagedProduct);
                dryStorage.setExtent(String.format("%.3f",oldExtent-(Float.parseFloat(packagedProduct.getExtent())-oldPackExtent)).replace(",","."));
            }

            dryStorageService.save(dryStorage);
        }
        if(packagedProduct.getDeliveryDocumentation()!=null) {
            reloadAllExtentFields(packagedProduct.getDeliveryDocumentation());
            deliveryDocumentationService.checkHeightUnicValue(packagedProduct.getDeliveryDocumentation());
        }
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }

    @PostMapping("/addPackageProduct-{userId}-{breedId}")
    public String addPackageProduct(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,PackagedProduct product,
                                    String docId,String dryStorageId){
        product.setUserCompany(userCompanyService.findById(userId));
        PackagedProduct productDB = deliveryDocumentationService.addPackageProductToDeliveryDoc(docId,product);
        if (dryStorageId!=null){
            DryStorage dryStorage = dryStorageService.findById(Integer.parseInt(dryStorageId));
            if(breedId!=2) {
                dryStorage.setCountOfDesk(dryStorage.getCountOfDesk() - Integer.parseInt(productDB.getCountOfDesk()));
            }else {
                dryStorage.setExtent(String.format("%.3f",Float.parseFloat(dryStorage.getExtent())-Float.parseFloat(productDB.getExtent())).replace(",","."));
            }
            dryStorageService.save(dryStorage);
            productDB.setDryStorage(dryStorage);
            packagedProductService.save(productDB);
        }
        reloadAllExtentFields(productDB.getDeliveryDocumentation());
        deliveryDocumentationService.checkHeightUnicValue(productDB.getDeliveryDocumentation());
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }

    @PostMapping("/deletePackageProduct-{userId}-{breedId}")
    public String deletePackageProduct(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    String id,String deliveryId){
        deliveryDocumentationService.deletePackage(id,deliveryId);
        DeliveryDocumentation doc = deliveryDocumentationService.findById(Integer.parseInt(deliveryId));
        reloadAllExtentFields(doc);
        deliveryDocumentationService.checkHeightUnicValue(doc);
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
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        btnConfig(userId,model);
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
//        ContrAgent contrAgent =  new ContrAgent();
//        contrAgent.setNameOfAgent(nameOfAgent);
//        treeStorage.setContrAgent(contrAgent);
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
//        ContrAgent contrAgent =  new ContrAgent();
//        contrAgent.setNameOfAgent(nameOfAgent);
//        treeStorage.setContrAgent(contrAgent);
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
