package com.swida.documetation.controllers;

import com.swida.documetation.data.dto.CellDryingStorageDto;
import com.swida.documetation.data.dto.storages.*;
import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.*;
import com.swida.documetation.data.service.LoggerDataInfoService;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.*;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import com.swida.documetation.data.service.subObjects.DriverInfoService;
import com.swida.documetation.utils.other.GenerateResponseForExport;
import com.swida.documetation.utils.xlsParsers.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
    private QualityStatisticInfoService statisticInfoService;
    private LoggerDataInfoService  loggerDataInfoService;

    @Autowired
    public FabricController(TreeStorageService treeStorageService, RawStorageService rawStorageService,
                            DryingStorageService dryingStorageService, DryStorageService dryStorageService,
                            PackagedProductService packagedProductService, DeliveryDocumentationService deliveryDocumentationService,
                            BreedOfTreeService breedOfTreeService, ContrAgentService contrAgentService,
                            UserCompanyService userCompanyService, OrderInfoService orderInfoService,
                            DriverInfoService driverInfoService, QualityStatisticInfoService statisticInfoService, LoggerDataInfoService loggerDataInfoService) {
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
        this.statisticInfoService = statisticInfoService;
        this.loggerDataInfoService = loggerDataInfoService;
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
        model.addAttribute("mainTreeStorage",treeStorageService.getMainTreeStorage(breedId,userId));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        btnConfig(userId,model);

        return "fabricPage";
    }

    @PostMapping("/addTreeStorageRow-{userId}-{breedId}")
    public String  addTreeStorageObj(@PathVariable("userId")int userId,
                                     @PathVariable("breedId")int breedId, TreeStorage treeStorage){

        TreeStorage saved = treeStorageService.putNewTreeStorageObj(breedId,userId,treeStorage);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.TREE,LoggerOperationType.CREATING,null,TreeStorageDTO.convertToDTO(saved));
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/returnQualityInfoObject-{userId}-{breedId}")
    public String  returnQualityInfoObject(int id,@PathVariable int breedId,@PathVariable int userId){
        QualityStatisticInfoDTO before = QualityStatisticInfoDTO.convertToDTO(statisticInfoService.findById(id));
        loggerDataInfoService.save(breedOfTreeService.findById(breedId), StorageType.TREE_STATISTIC, LoggerOperationType.RETURNING,
                before,null);
        statisticInfoService.returnQualityInfo(id);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editQualityInfoObject-{userId}-{breedId}")
    public String  editQualityInfoObject(@PathVariable int breedId,@PathVariable int userId,QualityStatisticInfo info){
        QualityStatisticInfoDTO before = QualityStatisticInfoDTO.convertToDTO(statisticInfoService.findById(info.getId()));
        List<QualityStatisticInfo> list =  statisticInfoService.edit(info);
        QualityStatisticInfoDTO after = new QualityStatisticInfoDTO();
        if(list.size()==1){
            after = QualityStatisticInfoDTO.convertToDTO(list.get(0));
        }
        loggerDataInfoService.save(breedOfTreeService.findById(breedId), StorageType.TREE_STATISTIC, LoggerOperationType.UPDATING,
                before,after);
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
        rawStorage.setUsedExtent(String.format("%.3f",Float.parseFloat(mainExtentTreeStorage)-Float.parseFloat(extentOfTreeStorage)).replace(",","."));
        rawStorageService.save(rawStorage);

        TreeStorage recycle = new TreeStorage();
        if(extentOfWaste==null){
            recycle.setExtent("0.000");
        }else {
            recycle.setExtent(String.format("%.3f", Float.parseFloat(extentOfWaste)).replace(',', '.'));
        }
        recycle.setMaxExtent(recycle.getExtent());
        recycle.setCodeOfProduct(treeStorage.getCodeOfProduct()+"-rec");
        recycle.setStatusOfTreeStorage(StatusOfTreeStorage.RECYCLING);
        recycle.setContrAgent(treeStorage.getContrAgent());

        recycle.setUserCompany(userCompanyService.findById(userId));
        recycle.setBreedOfTree(breedOfTreeService.findById(breedId));
        recycle.setBreedDescription(treeStorage.getBreedDescription());

        treeStorageService.save(recycle);
        rawStorage.setRecycle(recycle);
        treeStorage.getRecycle().add(recycle);
        rawStorage.setMaxExtent(rawStorage.getExtent());
        treeStorageService.save(treeStorage);
        rawStorageService.save(rawStorage);
        rawStorageService.checkQualityInfo(rawStorage);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editTreeStorageRow-{userId}-{breedId}")
    public String editTreeStorageRow(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, TreeStorage treeStorage){
        TreeStorageDTO before = TreeStorageDTO.convertToDTO(treeStorageService.findById(treeStorage.getId()));
        treeStorage.setExtent(String.format("%.3f", Float.parseFloat(treeStorage.getExtent())).replace(',', '.'));
        TreeStorage saved = treeStorageService.putNewTreeStorageObj(breedId,userId,treeStorage);
        TreeStorageDTO after = TreeStorageDTO.convertToDTO(saved);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.TREE,LoggerOperationType.UPDATING,before,after);
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
            filePath = parser.parse(startDate, endDate,breedId);
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
        rawStorage.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
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

        String rawExtent =  rawStorageService.save(rawStorage).getExtent();
        rawStorage.setUsedExtent(rawExtent);
        rawStorage.setMaxExtent(rawExtent);
        RawStorage after = rawStorageService.save(rawStorage);

        treeStorage.setMaxExtent(rawExtent);
        treeStorageService.save(treeStorage);

        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW,LoggerOperationType.CREATING,null, RawStorageDTO.convertToDTO(after));

        JSONObject json = new JSONObject();
        json.put("rawStorageId",rawStorage.getId());
        json.put("treeStorageExtent",treeStorage.getExtent());
        json.put("rawStorageExtent",rawStorage.getExtent());
        return json;
//        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }


    //RawStorage page

    @GetMapping("/getListOfRawStorage-{userId}-{breedId}")
    public String getListOfRawStorage(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,
                                      Model model,String[] descriptions, String[] heights, String[] longs,
                                      String[] widths,HttpServletRequest request){
        model.addAttribute("fragmentPathTabRawStorage","rawStorage");
        model.addAttribute("tabName","rawStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        List<RawStorage> rawStorageList = rawStorageService.getFilteredList(breedId,userId,descriptions,heights,longs,widths);
        model.addAttribute("rawStorageList",rawStorageList);
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        model.addAttribute("contrAgentList",contrAgentService.getListByType(ContrAgentType.PROVIDER));

        model.addAttribute("descList",rawStorageService.getListOfUnicBreedDescription(breedId));
        model.addAttribute("sizeOfHeightList",rawStorageService.getListOfUnicSizeOfHeight(breedId));
        model.addAttribute("sizeOfWidthList",rawStorageService.getListOfUnicSizeOfWidth(breedId));
        model.addAttribute("sizeOfLongList",rawStorageService.getListOfUnicSizeOfLong(breedId));
        model.addAttribute("exportLinkParams", "?" + request.getQueryString());
        model.addAttribute("sumExtent",rawStorageService.countExtent(rawStorageList).setScale(3, RoundingMode.DOWN).doubleValue());
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
        DryingStorage storage = dryingStorageService.save(dryingStorage);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRYING,LoggerOperationType.SENDING,null,DryingStorageDTO.convertToDTO(storage));
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editRawStorageObj-{userId}-{breedId}")
    public String editRawStorageObj(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    RawStorage rawStorage){

        RawStorage rawStorageDB = rawStorageService.findById(rawStorage.getId());
        RawStorageDTO before = RawStorageDTO.convertToDTO(rawStorageDB);
        float oldExtent = Float.parseFloat(rawStorageDB.getExtent());
        float newExtent = Float.parseFloat(rawStorage.getExtent());
        rawStorageDB.setBreedDescription(rawStorage.getBreedDescription());
        rawStorageDB.setCodeOfProduct(rawStorage.getCodeOfProduct());
        rawStorageDB.setCountOfDesk(rawStorage.getCountOfDesk());
        rawStorageDB.setSizeOfHeight(rawStorage.getSizeOfHeight());
        rawStorageDB.setSizeOfLong(rawStorage.getSizeOfLong());
        rawStorageDB.setSizeOfWidth(rawStorage.getSizeOfWidth());
        RawStorage saved = rawStorageService.save(rawStorageDB);

        TreeStorage treeStorage = rawStorageDB.getTreeStorage();
        if (treeStorage!=null) {
            treeStorage.setExtent(
                    String.format("%.3f", Float.parseFloat(treeStorage.getExtent()) - (Float.parseFloat(rawStorageService.findById(rawStorageDB.getId()).getExtent()) - oldExtent)).replace(",", ".")
            );
            if (treeStorage.getStatusOfTreeStorage() == StatusOfTreeStorage.PROVIDER_DESK) {
                treeStorage.setMaxExtent(
                        String.format("%.3f", Float.parseFloat(treeStorage.getMaxExtent()) + (Float.parseFloat(rawStorageService.findById(rawStorageDB.getId()).getExtent()) - oldExtent)).replace(",", ".")
                );
                treeStorage.setExtent("0.000");
                rawStorageDB.setMaxCountOfDesk(rawStorage.getCountOfDesk());
                saved = rawStorageService.save(rawStorageDB);
            }
            treeStorageService.save(treeStorage);
        }

        RawStorageDTO after = RawStorageDTO.convertToDTO(saved);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW,LoggerOperationType.UPDATING,before,after);
//        rawStorageService.checkQualityInfo(rawStorageDB);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/groupPineRaw-{userId}-{breedId}")
    public String groupPineRaw(@PathVariable int userId,@PathVariable int breedId,RawStorage rawStorage,Integer[] idOfRow){
        RawStorageListDTO before = RawStorageListDTO.convertToDTO(rawStorageService.findById(idOfRow));
        RawStorage afterRaw = rawStorageService.collectToOnePineEntity(rawStorage,idOfRow,userId,breedId);
        RawStorageDTO after = RawStorageDTO.convertToDTO(afterRaw);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW, LoggerOperationType.GROUP_ITEMS,before,after);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }
    @PostMapping("/groupOakRaw-{userId}-{breedId}")
    public String groupOakRaw(@PathVariable int userId,@PathVariable int breedId,RawStorage rawStorage,Integer[] idOfRow){
        RawStorageListDTO before = RawStorageListDTO.convertToDTO(rawStorageService.findById(idOfRow));
        RawStorage afterRaw = rawStorageService.collectToOneOakEntity(rawStorage,idOfRow,userId,breedId);
        RawStorageDTO after = RawStorageDTO.convertToDTO(afterRaw);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW, LoggerOperationType.GROUP_ITEMS,before,after);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/returnRawPackageToTree-{userId}-{breedId}")
    public String returnRawPackageToTree(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    String id){
        RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(id));
        RawStorageDTO before = RawStorageDTO.convertToDTO(rawStorage);
        if(rawStorage.getGroupedElements()==null || rawStorage.getGroupedElements().size()==0) {
            loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW,LoggerOperationType.RETURNING,before,null);
            TreeStorage treeStorage = rawStorage.getTreeStorage();
            float rawStorageExtent = Float.parseFloat(rawStorage.getExtent());

            if (treeStorage!=null && treeStorage.getStatusOfTreeStorage() == StatusOfTreeStorage.PROVIDER_DESK && treeStorage.getOrderInfo() != null) {
                OrderInfo orderInfo = treeStorage.getOrderInfo();
                orderInfo.setDoneExtendOfOrder(
                        String.format("%.3f",
                                Float.parseFloat(orderInfo.getDoneExtendOfOrder()) - Float.parseFloat(rawStorage.getExtent()))
                                .replace(",", ".")
                );
                orderInfoService.save(orderInfo);
                orderInfoService.reloadMainOrderExtent(orderInfo.getMainOrder());
            }
            if (rawStorage.getRecycle() != null) {
                rawStorage.getRecycle().setExtent("0.000");
                treeStorageService.save(rawStorage.getRecycle());
            }
            if(treeStorage!=null){
                if(rawStorage.getUsedExtent().isEmpty()){
                    rawStorage.setUsedExtent("0");
                }
                treeStorage.setExtent(
                        String.format("%.3f", Float.parseFloat(treeStorage.getExtent())
                                + Float.parseFloat(rawStorage.getUsedExtent())).replace(",", ".")
                );
                treeStorageService.save(treeStorage);
            }
            rawStorage.setExtent("0.000");
            rawStorage.setCountOfDesk(0);
            rawStorageService.save(rawStorage);
//            if(rawStorage.getTreeStorage()!=null && rawStorage.getTreeStorage().getContrAgent()!=null ){
//            rawStorageService.checkQualityInfo(rawStorage);
//            }
        }else {
            RawStorageListDTO after = RawStorageListDTO.convertToDTO(rawStorageService.uncollectFromOnePineEntity(rawStorage,userId,breedId));
            loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW,LoggerOperationType.UNGROUP_ITEMS,before,after);
        }

        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }


    @PostMapping("/exportRawStorageXLS-{userId}-{breedId}")
    public ResponseEntity<Resource> exportRawStorageXLS(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                                        String[] descriptions, String[] heights, String[] longs,
                                                        String[] widths) throws FileNotFoundException, ParseException {
        List<RawStorage> rawStorageList = rawStorageService.getFilteredList(breedId,userId,descriptions,heights,longs,widths);
        ParseRawStorageToXLS parser = new ParseRawStorageToXLS(rawStorageList);
        String filePath;
        if (breedId==2){
//            for breed oak
             filePath = parser.parseOAK();
        }else {
             filePath = parser.parse();
        }
       return new GenerateResponseForExport().generate(filePath,"","");
    }


    @PostMapping("/createRawPackages-{userId}-{breedId}")
    public String createRawPackages(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,String id,
                                 String codeOfProduct,int countOfDesk,String breedDescription,String height, String width, String count, String longFact, String heightWidth){

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

        packagedProductService.createPackages(String.valueOf(dryStorage.getId()),codeOfProduct+"raw",breedDescription,String.valueOf(countOfDesk),height,width,count,longFact,heightWidth,userCompanyService.findById(userId));
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
        model.addAttribute("uniqCell",
                CellDryingStorageDto.convert(dryingStorageService.getListByUserByBreed(breedId,userId)));
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
    @PostMapping("/addDeskToDryStorageMultiple-{userId}-{breedId}")
    private String addDeskToDryStorageMultiple(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                       Integer cellId){

        if(cellId!=null) {
            List<DryingStorage> dryingStorageDBList = dryingStorageService.getListByUserByBreed(breedId, userId).stream()
                    .filter(dr -> dr.getCell().equals(cellId)).collect(Collectors.toList());
            for(DryingStorage dryingStorageDB:dryingStorageDBList) {
                DryStorage dryStorage = new DryStorage();
                dryStorage.setCodeOfProduct(dryingStorageDB.getCodeOfProduct());

                dryStorage.setBreedOfTree(dryingStorageDB.getBreedOfTree());
                dryStorage.setBreedDescription(dryingStorageDB.getBreedDescription());
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
            }
        }
        return "redirect:/fabric/getListOfDryingStorage-"+userId+"-"+breedId;
    }


    @PostMapping("/editDryingStorageObj-{userId}-{breedId}")
    public String editDryingStorageObj(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                    DryingStorage dryingStorage ){
        DryingStorageDTO before = DryingStorageDTO.convertToDTO(dryingStorageService.findById(dryingStorage.getId()));
        DryingStorage after = dryingStorageService.editDryingStorage(dryingStorage);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRYING,LoggerOperationType.UPDATING,
                before,DryingStorageDTO.convertToDTO(after));
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
//    @GetMapping("/getListOfDryStorage-{userId}-{breedId}")
//    public String getListOfDryStorage(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
//                                      Model model){
//        model.addAttribute("fragmentPathTabDryStorage","dryStorage");
//        model.addAttribute("tabName","dryStorage");
//        model.addAttribute("userId",userId);
//        model.addAttribute("breedId",breedId);
//        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
//        model.addAttribute("dryStorageList",dryStorageService.getListByUserByBreed(breedId,userId));
//        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
//        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
//        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
//        btnConfig(userId,model);
//        return "fabricPage";
//    }

    @GetMapping("/getListOfDryStorage-{userId}-{breedId}")
    public String getListOfDryStorage(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                      Model model, String[] descriptions, String[] heights, String[] longs, String[] widths,
                                      HttpServletRequest request){
        model.addAttribute("fragmentPathTabDryStorage","dryStorage");
        model.addAttribute("tabName","dryStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        List<DryStorage> dryStorageList = dryStorageService.getFilteredList(breedId,userId,descriptions,heights,longs,widths);
        model.addAttribute("dryStorageList",dryStorageList);
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());

        model.addAttribute("descList",dryStorageService.getListOfUnicBreedDescription(breedId));
        model.addAttribute("sizeOfHeightList",dryStorageService.getListOfUnicSizeOfHeight(breedId));
        model.addAttribute("sizeOfWidthList",dryStorageService.getListOfUnicSizeOfWidth(breedId));
        model.addAttribute("sizeOfLongList",dryStorageService.getListOfUnicSizeOfLong(breedId));
        model.addAttribute("exportLinkParams", "?" + request.getQueryString());
        model.addAttribute("sumExtent",dryStorageService.countExtent(dryStorageList).setScale(3, RoundingMode.DOWN).doubleValue());
        btnConfig(userId,model);
        return "fabricPage";
    }

    @PostMapping("/editDryStorageRow-{userId}-{breedId}")
    public String editDryStorageRow(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,DryStorage dryStorage){
        DryStorageDTO before = DryStorageDTO.convertToDTO(dryStorageService.findById(dryStorage.getId()));
        DryStorage saved = dryStorageService.editDryStorage(dryStorage);
        DryStorageDTO after = DryStorageDTO.convertToDTO(saved);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRY,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/addDryStorageWithoutParent-{userId}-{breedId}")
    public String addDryStorageWithoutParent(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,DryStorage dryStorage){
        DryStorageDTO after = DryStorageDTO.convertToDTO(dryStorageService.addDryStorageWithoutParent(userId,breedId,dryStorage));
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRY,LoggerOperationType.CREATING,null,after);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/createPackages-{userId}-{breedId}")
    public String createPackages(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,String id,
                                    String codeOfProduct,String breedDescription,String countOfDesk,String height, String width, String count, String longFact, String heightWidth){
        packagedProductService.createPackages(id,codeOfProduct,breedDescription,countOfDesk,height,width,count,longFact,heightWidth,userCompanyService.findById(userId));


        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }
    @PostMapping("/groupPineDry-{userId}-{breedId}")
    public String groupPineDry(@PathVariable int userId,@PathVariable int breedId,DryStorage dryStorage,Integer[] idOfRow){
        dryStorageService.collectToOnePineEntityDry(dryStorage,idOfRow,userId,breedId);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }
    @PostMapping("/groupOakDry-{userId}-{breedId}")
    public String groupOakDry(@PathVariable int userId,@PathVariable int breedId,DryStorage dryStorage,Integer[] idOfRow){
        DryStorageListDTO before  = DryStorageListDTO.convertToDTO(dryStorageService.findById(idOfRow));
        DryStorage saved = dryStorageService.collectToOneOakEntityDry(dryStorage,idOfRow,userId,breedId);
        DryStorageDTO after = DryStorageDTO.convertToDTO(saved);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRY,LoggerOperationType.GROUP_ITEMS,before,after);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/ungroupPineDry-{userId}-{breedId}")
    public String ungroupPineDry(@PathVariable int userId,@PathVariable int breedId,int id){
        DryStorage dryStorage = dryStorageService.findById(id);
        DryStorageDTO before = DryStorageDTO.convertToDTO(dryStorage);
        List<DryStorage> afterList = dryStorageService.uncollectFromOnePineEntityDry(dryStorage,userId,breedId);
        DryStorageListDTO after = DryStorageListDTO.convertToDTO(afterList);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRY,LoggerOperationType.UNGROUP_ITEMS,before,after);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/exportDryStorageXLS-{userId}-{breedId}")
    public ResponseEntity<Resource> exportDryStorageXLS(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, String startDate,
                                                           String endDate,String[] descriptions, String[] heights, String[] longs, String[] widths) throws FileNotFoundException, ParseException {
        ParseDryStorageToXLS parser = new ParseDryStorageToXLS(dryStorageService.getFilteredList(breedId,userId,descriptions,heights,longs,widths));
        String filePath;
        if(startDate!=null && !startDate.isEmpty() && endDate!=null && !endDate.isEmpty()) {
            filePath = parser.parse(startDate, endDate, breedId);
        }else {
            if (breedId == 2) {
                filePath = parser.parseOAK();
            } else {
                filePath = parser.parse();
            }
        }
        return new GenerateResponseForExport().generate(filePath,startDate,endDate);
    }


    //Packaged product page
    @GetMapping("/getListOfPackagedProduct-{userId}-{breedId}")
    public String getListOfPackagedProduct(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                           Model model,String[] descriptions, String[] heights, String[] longs, String[] widths,
                                           HttpServletRequest request){
        UserCompany company = userCompanyService.findById(userId);
        ContrAgent contrAgent = company.getContrAgent();

        model.addAttribute("fragmentPathTabPackageStorage","packageStorage");
        model.addAttribute("tabName","packageStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);

        List<PackagedProduct> productList=packagedProductService.getFilteredList(breedId,userId,descriptions,heights,longs,widths);
        model.addAttribute("contractList",orderInfoService.getOrdersListByAgentByBreed(contrAgent.getId(),breedId));
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("packagedProductsList",productList);
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("deliveryList",deliveryDocumentationService.getListByUserByBreed(breedId,userId));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());

        model.addAttribute("descList",packagedProductService.getListOfUnicBreedDescription(breedId));
        model.addAttribute("sizeOfHeightList",packagedProductService.getListOfUnicSizeOfHeight(breedId));
        model.addAttribute("sizeOfWidthList",packagedProductService.getListOfUnicSizeOfWidth(breedId));
        model.addAttribute("sizeOfLongList",packagedProductService.getListOfUnicSizeOfLong(breedId));
        model.addAttribute("exportLinkParams", "?" + request.getQueryString());
        model.addAttribute("sumExtent",packagedProductService.countExtent(productList).setScale(3, RoundingMode.DOWN).doubleValue());
        btnConfig(userId,model);
        return "fabricPage";
    }

    @PostMapping("/exportPackageStorageXLS-{userId}-{breedId}")
    public ResponseEntity<Resource> exportRawStorageXLS(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                                        String startDate, String endDate,String[] descriptions,
                                                        String[] heights, String[] longs, String[] widths,
                                                        String[] qualities) throws FileNotFoundException, ParseException {
        List<PackagedProduct> productList;
        System.out.println(Arrays.toString(widths));
        String filePath;
        if (breedId==2){
//            for breed oak
            productList = packagedProductService.getFilteredListOak(breedId,userId,qualities,heights,longs);
            ParserOakPackagerProductXLS parser =  new ParserOakPackagerProductXLS(productList);
            if(startDate==null || startDate.isEmpty() || endDate==null || endDate.isEmpty()){
                filePath = parser.parse();
            }else {
                filePath = parser.parse(startDate,endDate);
            }
        }else {
            productList = packagedProductService.getFilteredList(breedId,userId,descriptions,heights,longs,widths);
            ParserPackagedProductToXLS parser =  new ParserPackagedProductToXLS(productList);
            if(startDate==null || startDate.isEmpty() || endDate==null || endDate.isEmpty()){
                filePath = parser.parse();
            }else {
                filePath = parser.parse(startDate,endDate);
            }
        }
        System.out.println("size "+ productList.size());
        return new GenerateResponseForExport().generate(filePath,"","");
    }

    @PostMapping("/unformPackagedProduct-{userId}-{breedId}")
    public  String unformPackagedProduct(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, Integer[] id){
        PackageProductListDTO before = PackageProductListDTO.convertToDTO(packagedProductService.findById(id));
        packagedProductService.unformPackageProduct(breedId,userId,id);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.PACKAGE,LoggerOperationType.RETURNING,before,null);
        return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+breedId;
    }


    @PostMapping("/addPackagedProductWithoutHistory-{userId}-{breedId}")
    public String addPackagedProductWithoutHistory(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                                  PackagedProduct product, String countOfPacks){
        product.setDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        packagedProductService.createPackagesWithoutHistory(product,countOfPacks,breedId,userId);
        return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+breedId;
    }

    @PostMapping("/editPack-{userId}-{breedId}")
    public String editPack(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,PackagedProduct product){
        int oldCountOFDesk = Integer.parseInt(packagedProductService.findById(product.getId()).getCountOfDesk());
        PackagedProduct productDB = packagedProductService.editPackageProduct(product);
        productDB.setExtent(packagedProductService.countExtent(productDB));
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
        System.out.println("delete pack id "+id+" delivery id "+deliveryId);
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
                                     @PathVariable("breedId")int breedId,TreeStorage treeStorage){
        treeStorage.setStatusOfTreeStorage(StatusOfTreeStorage.RECYCLING);
        treeStorageService.putNewTreeStorageObj(breedId,userId,treeStorage);
        return "redirect:/fabric/getListOfRecycle-"+userId+"-"+breedId;
    }


    @PostMapping("/cutOfRecycle-{userId}-{breedId}")
    public String  cutOfRecycle(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId,
                                          int idOfTreeStorageRow, String extentOfTreeStorage, RawStorage rawStorage){
        TreeStorage treeStorage = treeStorageService.findById(idOfTreeStorageRow);

        rawStorage.setUsedExtent(
                String.format("%.3f",Float.parseFloat(treeStorage.getExtent())-Float.parseFloat(extentOfTreeStorage)).replace(',','.')
        );
        treeStorage.setExtent(String.format("%.3f",Float.parseFloat(extentOfTreeStorage)).replace(',','.'));
        rawStorage.setTreeStorage(treeStorage);
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setBreedDescription(treeStorage.getBreedDescription());
        rawStorageService.save(rawStorage);
        rawStorage.setMaxExtent(rawStorage.getExtent());
        rawStorageService.save(rawStorage);
        return "redirect:/fabric/getListOfRecycle-"+userId+"-"+breedId;
    }

    @PostMapping("/editRecycleRow-{userId}-{breedId}")
    public String editRecycleRow(@PathVariable("userId")int userId, @PathVariable("breedId")int breedId, TreeStorage treeStorage){
        treeStorage.setStatusOfTreeStorage(StatusOfTreeStorage.RECYCLING);
        treeStorageService.putNewTreeStorageObj(breedId,userId,treeStorage);
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
