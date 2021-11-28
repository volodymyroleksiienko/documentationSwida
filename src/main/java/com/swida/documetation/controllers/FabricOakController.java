package com.swida.documetation.controllers;

import com.google.gson.Gson;
import com.swida.documetation.data.dto.CellDryingStorageDto;
import com.swida.documetation.data.dto.storages.*;
import com.swida.documetation.data.dto.subObjects.DeliveryDocumentationDTO;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.ContrAgentType;
import com.swida.documetation.data.enums.LoggerOperationType;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import com.swida.documetation.data.enums.StorageType;
import com.swida.documetation.data.service.LoggerDataInfoService;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.*;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

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
    private LoggerDataInfoService loggerDataInfoService;

    @Autowired
    public FabricOakController(TreeStorageService treeStorageService, RawStorageService rawStorageService,
                               DryingStorageService dryingStorageService, DryStorageService dryStorageService,
                               PackagedProductService packagedProductService, DeliveryDocumentationService deliveryDocumentationService,
                               BreedOfTreeService breedOfTreeService, ContrAgentService contrAgentService,
                               UserCompanyService userCompanyService, OrderInfoService orderInfoService,
                               DescriptionDeskOakService deskOakService, LoggerDataInfoService loggerDataInfoService) {
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
    //Tree Storage page
    @GetMapping("/getListOfTreeStorage-{userId}-2")
    public String getListOfTreeStorage(@PathVariable("userId")int userId, Model model){
        int breedId = 2;
        model.addAttribute("fragmentPathTabTreeStorage","treeStorageOAK");
        model.addAttribute("tabName","treeStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        List<TreeStorage> treeStorageList = treeStorageService.getListByUserByBreed(breedId,userId, StatusOfTreeStorage.TREE);
        model.addAttribute("treeStorageList",treeStorageList);
        model.addAttribute("orderList",orderInfoService.getOrdersListByBreed(breedId));
        model.addAttribute("contrAgentList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        model.addAttribute("mainTreeStorage",treeStorageService.getMainTreeStorage(breedId,userId));

        btnConfig(userId,model);
        return "fabricPage";
    }




    @PostMapping("/cutOfTreeStorage-{userId}-2")
    public String  addCutTreeToRawStorage(@PathVariable("userId")int userId, String idOfTreeStorageRow,
                                         RawStorage rawStorage, String usedExtent,String extentOfWaste){
        int breedId = 2;

        TreeStorage treeStorage = treeStorageService.findById(Integer.parseInt(idOfTreeStorageRow));

        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setTreeStorage(treeStorage);
        rawStorage.setMaxExtent(rawStorage.getExtent());
        String rawStorageExtent = rawStorageService.save(rawStorage).getExtent();
        treeStorage.setExtent(String.format("%.3f",Float.parseFloat(treeStorage.getExtent())-Float.parseFloat(usedExtent)).replace(',','.'));

//        TreeStorage recycle = new TreeStorage();
//        if(extentOfWaste==null){
//            recycle.setExtent("0.000");
//        }else {
//            recycle.setExtent(String.format("%.3f", Float.parseFloat(extentOfWaste)).replace(',', '.'));
//        }
//        recycle.setMaxExtent(recycle.getExtent());
//        recycle.setCodeOfProduct(treeStorage.getCodeOfProduct()+"-rec");
//        recycle.setStatusOfTreeStorage(StatusOfTreeStorage.RECYCLING);
//        recycle.setContrAgent(treeStorage.getContrAgent());
//
//        recycle.setUserCompany(userCompanyService.findById(userId));
//        recycle.setBreedOfTree(breedOfTreeService.findById(breedId));
//        recycle.setBreedDescription(treeStorage.getBreedDescription());

//        treeStorageService.save(recycle);
//        treeStorage.getRecycle().add(recycle);
//        rawStorage.setRecycle(recycle);
        treeStorageService.save(treeStorage);
        rawStorageService.checkQualityInfo(rawStorage);
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editTreeStorageRow-{userId}-2")
    public String editTreeStorageRow(@PathVariable("userId")int userId, String nameOfAgent, TreeStorage treeStorage){
        int breedId = 2;
        TreeStorageDTO before = TreeStorageDTO.convertToDTO(treeStorageService.findById(treeStorage.getId()));
        treeStorage.setExtent(String.format("%.3f", Float.parseFloat(treeStorage.getExtent())).replace(',', '.'));
        TreeStorage treeStorageFromDB = treeStorageService.putNewTreeStorageObj(breedId,userId,treeStorage);
        loggerDataInfoService.save(breedOfTreeService.findById(2),StorageType.TREE, LoggerOperationType.UPDATING,
                before,TreeStorageDTO.convertToDTO(treeStorageFromDB));
        return "redirect:/fabric/getListOfTreeStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/addDeskFromProvider-{userId}-2")
    public String addDeskFromProvider(@PathVariable("userId")int userId, TreeStorage treeStorage, String sizeOfHeight,
                                      String description, String nameOfAgentId, String extent,String contractId,String sizeOfLong){
       int breedId = 2;

        treeStorage.setUserCompany(userCompanyService.findById(userId));
        treeStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        treeStorage.setExtent("0.000");
        treeStorage.setContrAgent(contrAgentService.findById(Integer.parseInt(nameOfAgentId)));

//        OrderInfo orderInfo = orderInfoService.findById(Integer.parseInt(contractId));
//        treeStorage.setOrderInfo(orderInfo);
//        treeStorage.setContrAgent(orderInfo.getContrAgent());

        treeStorage.setStatusOfTreeStorage(StatusOfTreeStorage.PROVIDER_DESK);
        treeStorageService.putNewTreeStorageObj(breedId,userId,treeStorage);

        RawStorage rawStorage = new RawStorage();
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setTreeStorage(treeStorage);

        rawStorage.setCodeOfProduct(treeStorage.getCodeOfProduct());
        rawStorage.setBreedDescription(treeStorage.getBreedDescription());
        rawStorage.setExtent(extent);
        rawStorage.setMaxExtent(extent);

        rawStorage.setSizeOfHeight(sizeOfHeight);
        rawStorage.setSizeOfLong(sizeOfLong);
        rawStorage.setDescription(description);

        RawStorage after = rawStorageService.save(rawStorage);

        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW,LoggerOperationType.CREATING,null, RawStorageDTO.convertToDTO(after));

//        orderInfo.setDoneExtendOfOrder(
//                String.format("%.3f",
//                        Float.parseFloat(orderInfo.getDoneExtendOfOrder())+Float.parseFloat(rawStorageService.findById(rawStorage.getId()).getExtent()))
//                        .replace(",",".")
//        );
//        orderInfoService.save(orderInfo);
//        orderInfoService.reloadMainOrderExtent(orderInfo.getMainOrder());
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }


    //RawStorage page
    @GetMapping("/getListOfRawStorage-{userId}-2")
    public String getListOfRawStorage(@PathVariable("userId")int userId, Model model,
                                      String[] descriptions, String[] heights, String[] longs,
                                      String[] widths,HttpServletRequest request,String sortedType,String sortedField){
        int breedId = 2;

        model.addAttribute("fragmentPathTabRawStorage","rawStorageOAK");
        model.addAttribute("tabName","rawStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        List<RawStorage> rawStorageList = rawStorageService.getFilteredList(breedId,userId,descriptions,heights,longs,widths);
        model.addAttribute("rawStorageList",rawStorageService.sortedBy(rawStorageList,sortedField,sortedType));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        model.addAttribute("contrAgentList",contrAgentService.getListByType(ContrAgentType.PROVIDER));

        model.addAttribute("treeStorageList", Collections.singletonList(treeStorageService.getMainTreeStorage(breedId,userId)));
        model.addAttribute("sortedField",sortedField);
        model.addAttribute("sortedType",sortedType);

        model.addAttribute("descList",rawStorageService.getListOfUnicBreedDescription(breedId));
        model.addAttribute("sizeOfHeightList",rawStorageService.getListOfUnicSizeOfHeight(breedId));
        model.addAttribute("sizeOfLongList",rawStorageService.getListOfUnicSizeOfLong(breedId));
        String requestParams = request.getQueryString();
        if(requestParams!=null) {
            requestParams = requestParams.replace("sortedField=date", "")
                    .replace("sortedField=code", "")
                    .replace("sortedField=description", "")
                    .replace("sortedField=breedDescription", "")
                    .replace("sortedField=height", "")
                    .replace("sortedField=long", "")
                    .replace("sortedField=extent", "")
                    .replace("sortedField=provider", "")
                    .replace("sortedType=ASC", "")
                    .replace("sortedType=DESC", "")
                    .replace("&&", "&")
                    .replace("&&&", "&");
        }
        model.addAttribute("exportLinkParams", "?" + requestParams);
        model.addAttribute("sumExtent",rawStorageService.countExtent(rawStorageList).setScale(3, RoundingMode.DOWN).doubleValue());
        btnConfig(userId,model);
        return "fabricPage";
    }
    @PostMapping("/addDeskToDrying-{userId}-2")
    private String addDeskToDrying(@PathVariable("userId")int userId, String rawStorageId, DryingStorage dryingStorage,
                                   String date){
        int breedId = 2;

        dryingStorage.setUserCompany(userCompanyService.findById(userId));
        dryingStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(rawStorageId));
        rawStorage.setExtent(String.format("%.3f",Float.parseFloat(rawStorage.getExtent())-Float.parseFloat(dryingStorage.getExtent())).replace(',','.'));

        dryingStorage.setSizeOfHeight(rawStorage.getSizeOfHeight());
        dryingStorage.setSizeOfLong(rawStorage.getSizeOfLong());
        dryingStorage.setBreedDescription(rawStorage.getBreedDescription());
        dryingStorage.setDateDrying(date);

        rawStorageService.save(rawStorage);
        dryingStorage.setRawStorage(rawStorage);
        DryingStorage savedDS = dryingStorageService.save(dryingStorage);
        List<DescriptionDeskOak>  deskOakList = new ArrayList<>();
        if(!rawStorage.getDeskOakList().isEmpty()){
            for(DescriptionDeskOak desc:rawStorage.getDeskOakList()){
                desc.setRawStorage(null);
                desc.setDryingStorage(dryingStorage);
                deskOakList.add(deskOakService.save(desc));
            }
//            rawStorageService.countExtentRawStorageWithDeskDescription(rawStorage);
        }
        savedDS.setDeskOakList(deskOakList);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRYING,LoggerOperationType.CREATING,null, DryingStorageDTO.convertToDTO(savedDS));
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editRawStorageObj-{userId}-2")
    public String editRawStorageObj(@PathVariable("userId")int userId, RawStorage rawStorage){
        int breedId = 2;

        RawStorage rawStorageDB = rawStorageService.findById(rawStorage.getId());
        RawStorageDTO before = RawStorageDTO.convertToDTO(rawStorageDB);
        float oldExtent = Float.parseFloat(rawStorageDB.getExtent());
        rawStorageDB.setBreedDescription(rawStorage.getBreedDescription());
        rawStorageDB.setCodeOfProduct(rawStorage.getCodeOfProduct());
        rawStorageDB.setExtent(rawStorage.getExtent());
        rawStorageDB.setSizeOfHeight(rawStorage.getSizeOfHeight());
        rawStorageDB.setSizeOfLong(rawStorage.getSizeOfLong());
        rawStorageDB.setDescription(rawStorage.getDescription());
        RawStorage savedRawStorage = rawStorageService.save(rawStorageDB);
        RawStorageDTO after = RawStorageDTO.convertToDTO(savedRawStorage);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW,LoggerOperationType.UPDATING,before,after);
        rawStorageService.countExtentRawStorageWithDeskDescription(rawStorageDB);

        TreeStorage treeStorage = rawStorageDB.getTreeStorage();
        if(treeStorage!=null) {
            treeStorage.setExtent(
                    String.format("%.3f", Float.parseFloat(treeStorage.getExtent()) - (Float.parseFloat(rawStorage.getExtent()) - oldExtent)).replace(",", ".")
            );
            if (treeStorage.getStatusOfTreeStorage() == StatusOfTreeStorage.PROVIDER_DESK) {
                treeStorage.setMaxExtent(
                        String.format("%.3f", Float.parseFloat(treeStorage.getMaxExtent()) + (Float.parseFloat(rawStorageService.findById(rawStorageDB.getId()).getExtent()) - oldExtent)).replace(",", ".")
                );
                treeStorage.setExtent("0.000");
            }
            treeStorageService.save(treeStorage);
//            is not using for foreign rows
//            rawStorageService.checkQualityInfo(rawStorage);
        }
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/addDescriptionOakItemToRawStorage-{userId}-{breedId}")
    public String addDescriptionOakItemToRawStorage(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,int positionId,String width, String count){
        RawStorage rawStorage = rawStorageService.findById(positionId);
        RawStorageDTO before = RawStorageDTO.convertToDTO(rawStorage);
        DescriptionDeskOak deskOak = new DescriptionDeskOak();
        deskOak.setSizeOfWidth(width);
        deskOak.setCountOfDesk(count);
        deskOak.setRawStorage(rawStorage);
        deskOakService.save(deskOak);
        rawStorage.getDeskOakList().add(deskOak);
        rawStorageService.countExtentRawStorageWithDeskDescription(rawStorage);
        RawStorageDTO after = RawStorageDTO.convertToDTO(rawStorageService.findById(positionId));
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editDescriptionOakItemToRawStorage-{userId}-{breedId}")
    public String editDescriptionOakItemToRawStorage(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,int rawStorageId,int descId,String width, String count){
        RawStorage rawStorage = rawStorageService.findById(rawStorageId);
        RawStorageDTO before = RawStorageDTO.convertToDTO(rawStorage);
        DescriptionDeskOak deskOak = deskOakService.findById(descId);
        deskOak.setSizeOfWidth(width);
        deskOak.setCountOfDesk(count);
        deskOakService.save(deskOak);
        rawStorageService.countExtentRawStorageWithDeskDescription(rawStorageService.findById(rawStorageId));
        RawStorageDTO after = RawStorageDTO.convertToDTO(rawStorageService.findById(rawStorageId));
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/deleteDescriptionOakItemToRawStorage-{userId}-{breedId}")
    public String deleteDescriptionOakItemToRawStorage(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,int rawStorageId,int descId){
        RawStorage rawStorage = rawStorageService.findById(rawStorageId);
        RawStorageDTO before = RawStorageDTO.convertToDTO(rawStorage);
        DescriptionDeskOak deskOak = deskOakService.findById(descId);
        rawStorage.getDeskOakList().remove(deskOak);
        deskOakService.deleteByID(descId);

        rawStorageService.countExtentRawStorageWithDeskDescription(rawStorage);
        RawStorageDTO after = RawStorageDTO.convertToDTO(rawStorageService.findById(rawStorageId));
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW,LoggerOperationType.UPDATING,before,after);
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
        model.addAttribute("dryingStorageList",dryingStorageService.getListByUserByBreed(breedId,userId).stream().sorted((o1, o2) -> o2.getId()-o1.getId()).collect(Collectors.toList()));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        model.addAttribute("uniqCell",
                CellDryingStorageDto.convert(dryingStorageService.getListByUserByBreed(breedId,userId)));

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
        dryStorage.setMaxExtent(dryingStorageDB.getExtent());
        dryStorage.setSizeOfHeight(dryingStorageDB.getSizeOfHeight());
        dryStorage.setSizeOfLong(dryingStorageDB.getSizeOfLong());
        dryStorage.setDescription(dryingStorageDB.getDescription());
        dryStorage.setUserCompany(dryingStorageDB.getUserCompany());
        dryStorage.setDryingStorage(dryingStorageDB);

        DryStorage savedDS = dryStorageService.save(dryStorage);
        dryingStorageDB.setExtent("0.000");
        dryingStorageService.save(dryingStorageDB);
        if(!dryingStorageDB.getDeskOakList().isEmpty()){
            for(DescriptionDeskOak desk: dryingStorageDB.getDeskOakList()){
                desk.setDryingStorage(null);
                desk.setDryStorage(dryStorage);
                deskOakService.save(desk);
            }
        }
        DryStorageListDTO after = DryStorageListDTO.convertToDTO(Collections.singletonList(savedDS));
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRY,LoggerOperationType.SENDING,null,after);
        return "redirect:/fabric/getListOfDryingStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/addDeskToDryStorageMultiple-{userId}-2")
    private String addDeskToDryStorageMultiple(@PathVariable("userId")int userId, Integer cellId){
        int breedId = 2;
        List<DryStorage> list = new ArrayList<>();
        if(cellId!=null) {
            List<DryingStorage> dryingStorageDBList = dryingStorageService.getListByUserByBreed(breedId, userId)
                    .parallelStream()
                    .filter(dr -> dr.getCell().equals(cellId)).collect(Collectors.toList());
            for (DryingStorage dryingStorageDB : dryingStorageDBList) {
                DryStorage dryStorage = new DryStorage();
                dryStorage.setCodeOfProduct(dryingStorageDB.getCodeOfProduct());

                dryStorage.setBreedOfTree(dryingStorageDB.getBreedOfTree());
                dryStorage.setBreedDescription(dryingStorageDB.getBreedDescription());
                dryStorage.setExtent(dryingStorageDB.getExtent());
                dryStorage.setMaxExtent(dryingStorageDB.getExtent());
                dryStorage.setSizeOfHeight(dryingStorageDB.getSizeOfHeight());
                dryStorage.setSizeOfLong(dryingStorageDB.getSizeOfLong());
                dryStorage.setDescription(dryingStorageDB.getDescription());
                dryStorage.setUserCompany(dryingStorageDB.getUserCompany());
                dryStorage.setDryingStorage(dryingStorageDB);
                if(dryingStorageDB.getDeskOakList()!=null && dryingStorageDB.getDeskOakList().size()>0) {
                    dryStorage.setWasWithDeskOakList(true);
                }

                DryStorage savedDS = dryStorageService.save(dryStorage);
                dryingStorageDB.setExtent("0.000");
                dryingStorageService.save(dryingStorageDB);
                List<DescriptionDeskOak> deskOakList = new ArrayList<>();
                if (!dryingStorageDB.getDeskOakList().isEmpty()) {
                    for (DescriptionDeskOak desk : dryingStorageDB.getDeskOakList()) {
                        desk.setDryingStorage(null);
                        desk.setDryStorage(dryStorage);
                        deskOakList.add(deskOakService.save(desk));
                    }
                }
                savedDS.setDeskOakList(deskOakList);
                list.add(savedDS);
            }
        }
        DryStorageListDTO after = DryStorageListDTO.convertToDTO(list);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRY,LoggerOperationType.SENDING,null,after);
        return "redirect:/fabric/getListOfDryingStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/addDescriptionOakItemToDryingStorage-{userId}-{breedId}")
    public String addDescriptionOakItemToDryingStorage(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,int dryStorageId,String width, String count){
        DryingStorage dryingStorage = dryingStorageService.findById(dryStorageId);
        DryingStorageDTO before = DryingStorageDTO.convertToDTO(dryingStorage);
        DescriptionDeskOak deskOak = new DescriptionDeskOak();
        deskOak.setSizeOfWidth(width);
        deskOak.setCountOfDesk(count);
        deskOakService.save(deskOak);
        dryingStorage.getDeskOakList().add(deskOak);
        dryingStorageService.countExtentRawStorageWithDeskDescription(dryingStorage);
        DryingStorageDTO after = DryingStorageDTO.convertToDTO(dryingStorageService.findById(dryStorageId));
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRYING,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfDryingStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editDescriptionOakItemToDryingStorage-{userId}-{breedId}")
    public String editDescriptionOakItemToDryingStorage(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,int dryingStorageId,int descId,String width, String count){
        DryingStorage dryingStorage = dryingStorageService.findById(dryingStorageId);
        DryingStorageDTO before = DryingStorageDTO.convertToDTO(dryingStorage);
        DescriptionDeskOak deskOak = deskOakService.findById(descId);
        deskOak.setSizeOfWidth(width);
        deskOak.setCountOfDesk(count);
        deskOakService.save(deskOak);
        dryingStorageService.countExtentRawStorageWithDeskDescription(dryingStorageService.findById(dryingStorageId));
        DryingStorageDTO after = DryingStorageDTO.convertToDTO(dryingStorageService.findById(dryingStorageId));
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRYING,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfDryingStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/deleteDescriptionOakItemToDryingStorage-{userId}-{breedId}")
    public String deleteDescriptionOakItemToDryingStorage(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,int dryingStorageId,int descId){
        DryingStorage dryingStorage = dryingStorageService.findById(dryingStorageId);
        DryingStorageDTO before = DryingStorageDTO.convertToDTO(dryingStorage);
        DescriptionDeskOak deskOak = deskOakService.findById(descId);
        dryingStorage.getDeskOakList().remove(deskOak);
        deskOakService.deleteByID(descId);
        dryingStorageService.countExtentRawStorageWithDeskDescription(dryingStorage);
        DryingStorageDTO after = DryingStorageDTO.convertToDTO(dryingStorageService.findById(dryingStorageId));
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRYING,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfDryingStorage-"+userId+"-"+breedId;
    }


    //Dry Storage page
    @GetMapping("/getListOfDryStorage-{userId}-2")
    public String getListOfDryStorage(@PathVariable("userId")int userId,Model model,String[] descriptions,
                                      String[] heights, String[] longs, String[] widths, HttpServletRequest request,
                                      String sortedField,String sortedType){
        int breedId = 2;

        model.addAttribute("fragmentPathTabDryStorage","dryStorageOAK");
        model.addAttribute("tabName","dryStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        List<DryStorage> dryStorageList = dryStorageService.getFilteredList(breedId,userId,descriptions,heights,longs,widths);
        for(DryStorage dryStorage :dryStorageList){
            PackagedProduct product = packagedProductService.getProductByDryStorage(dryStorage.getId());
            if(product!=null){
                dryStorage.setQualityOfPack(product.getQuality());
                dryStorage.setLongOfPack(product.getSizeOfLong());
                dryStorageService.save(dryStorage);
            }
        }
        String requestParams = request.getQueryString();
        if(requestParams!=null) {
            requestParams = requestParams.replace("sortedField=date", "")
                    .replace("sortedField=code", "")
                    .replace("sortedField=description", "")
                    .replace("sortedField=breedDescription", "")
                    .replace("sortedField=height", "")
                    .replace("sortedField=long", "")
                    .replace("sortedField=extent", "")
                    .replace("sortedField=provider", "")
                    .replace("sortedType=ASC", "")
                    .replace("sortedType=DESC", "")
                    .replace("&&", "&")
                    .replace("&&&", "&");
        }
        model.addAttribute("exportLinkParams", "?" + requestParams);
        model.addAttribute("sortedField", sortedField);
        model.addAttribute("sortedType",sortedType);


        model.addAttribute("dryStorageList",dryStorageService.sortedBy(dryStorageList,sortedField,sortedType));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());

        model.addAttribute("descList",dryStorageService.getListOfUnicBreedDescription(breedId));
        model.addAttribute("sizeOfHeightList",dryStorageService.getListOfUnicSizeOfHeight(breedId));
        model.addAttribute("sizeOfWidthList",dryStorageService.getListOfUnicSizeOfWidth(breedId));
        model.addAttribute("sizeOfLongList",dryStorageService.getListOfUnicSizeOfLong(breedId));
        model.addAttribute("sumExtent",dryStorageService.countExtent(dryStorageList).setScale(3, RoundingMode.DOWN).doubleValue());
        btnConfig(userId,model);
        return "fabricPage";
    }


    @PostMapping("/resetDryPackageExtent-{userId}-{breedId}")
    public String resetDryPackageExtent(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,String id) {
        DryStorage dryStorage = dryStorageService.findById(Integer.parseInt(id));
        DryStorageDTO before = DryStorageDTO.convertToDTO(dryStorage);
        dryStorage.setExtent("0.000");
        dryStorageService.save(dryStorage);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRY, LoggerOperationType.RETURN_TO_ZERO,before,null);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/resetRawPackageExtent-{userId}-{breedId}")
    public String resetRawPackageExtent(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,String id) {
        RawStorage rawStorage = rawStorageService.findById(Integer.parseInt(id));
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.RAW, LoggerOperationType.RETURN_TO_ZERO,RawStorageDTO.convertToDTO(rawStorage),null);
        rawStorage.setExtent("0.000");
        rawStorageService.save(rawStorage);
//        rawStorageService.checkQualityInfo(rawStorage);
        return "redirect:/fabric/getListOfRawStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/addDescriptionOakItemToDryStorage-{userId}-{breedId}")
    public String addDescriptionOakItemToDryStorage(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,int dryStorageId,String width, String count){
        DryStorage dryStorage = dryStorageService.findById(dryStorageId);
        DryStorageDTO before = DryStorageDTO.convertToDTO(dryStorage);
        DescriptionDeskOak deskOak = new DescriptionDeskOak();
        deskOak.setSizeOfWidth(width);
        deskOak.setCountOfDesk(count);
        deskOak.setDryStorage(dryStorage);
        deskOakService.save(deskOak);
        dryStorage.getDeskOakList().add(deskOak);
        DryStorage savedDS = dryStorageService.countExtentRawStorageWithDeskDescription(dryStorage);
        DryStorageDTO after = DryStorageDTO.convertToDTO(savedDS);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRY,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/editDescriptionOakItemToDryStorage-{userId}-{breedId}")
    public String editDescriptionOakItemToDryStorage(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,int dryStorageId,int descId,String width, String count){
        DryStorage dryStorage = dryStorageService.findById(dryStorageId);
        DryStorageDTO before = DryStorageDTO.convertToDTO(dryStorage);
        DescriptionDeskOak deskOak = deskOakService.findById(descId);
        deskOak.setSizeOfWidth(width);
        deskOak.setCountOfDesk(count);
        deskOakService.save(deskOak);
        DryStorage saved = dryStorageService.countExtentRawStorageWithDeskDescription(dryStorage);
        DryStorageDTO after = DryStorageDTO.convertToDTO(saved);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRY,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

    @PostMapping("/deleteDescriptionOakItemToDryStorage-{userId}-{breedId}")
    public String deleteDescriptionOakItemToDryStorage(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,int dryStorageId,int descId){
        DryStorage dryStorage = dryStorageService.findById(dryStorageId);
        DryStorageDTO before = DryStorageDTO.convertToDTO(dryStorage);
        DescriptionDeskOak deskOak = deskOakService.findById(descId);
        dryStorage.getDeskOakList().remove(deskOak);
        deskOakService.deleteByID(descId);
        DryStorage saved = dryStorageService.countExtentRawStorageWithDeskDescription(dryStorage);
        DryStorageDTO after = DryStorageDTO.convertToDTO(saved);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DRY,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfDryStorage-"+userId+"-"+breedId;
    }

        //Packaged product page
    @GetMapping("/getListOfPackagedProduct-{userId}-2")
    public String getListOfPackagedProduct(@PathVariable("userId")int userId, Model model,String[] qualities, String[] heights, String[] longs, String[] widths,
                                           HttpServletRequest request,String sortedField,String sortedType){
        int breedId = 2;
        model.addAttribute("fragmentPathTabPackageStorage","packageStorageOak");
        model.addAttribute("tabName","packageStorage");
        model.addAttribute("userId",userId);
        model.addAttribute("breedId",breedId);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        List<PackagedProduct> productList = packagedProductService.getFilteredListOak(breedId,userId,qualities,heights,longs).stream().sorted((o1, o2) -> o2.getId()-o1.getId()).collect(Collectors.toList());
        model.addAttribute("packageOakList",packagedProductService.sortedBy(productList,sortedField,sortedType));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("deliveryList",deliveryDocumentationService.getListByUserByBreed(breedId,userId));
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());

        UserCompany company = userCompanyService.findById(userId);
        ContrAgent contrAgent = company.getContrAgent();
        model.addAttribute("contractList",orderInfoService.getOrdersListByAgentByBreed(contrAgent.getId(),breedId));

        model.addAttribute("qualityList",packagedProductService.getListOfUnicQuality(breedId));
        model.addAttribute("sizeOfHeightList",packagedProductService.getListOfUnicSizeOfHeight(breedId));
        model.addAttribute("sizeOfLongList",packagedProductService.getListOfUnicSizeOfLong(breedId));
        model.addAttribute("sumExtent",packagedProductService.countExtent(productList).setScale(3, RoundingMode.DOWN).doubleValue());
        btnConfig(userId,model);

        String requestParams = request.getQueryString();
        if(requestParams!=null) {
            requestParams = requestParams.replace("sortedField=date", "")
                    .replace("sortedField=code", "")
                    .replace("sortedField=quality", "")
                    .replace("sortedField=height", "")
                    .replace("sortedField=long", "")
                    .replace("sortedField=sumWidth", "")
                    .replace("sortedField=extent", "")
                    .replace("sortedField=countOfDesk", "")
                    .replace("sortedType=ASC", "")
                    .replace("sortedType=DESC", "")
                    .replace("&&", "&")
                    .replace("&&", "&")
                    .replace("&&", "&")
                    .replace("&&", "&");
        }
        model.addAttribute("exportLinkParams", "?" + requestParams);
        model.addAttribute("sortedField", sortedField);
        model.addAttribute("sortedType",sortedType);

        return "fabricPage";
    }

    @PostMapping("/addDryStorageToPackageProduct-{userId}-2")
    public String addDryStorageToPackageProduct(@PathVariable("userId")int userId,DryStorage dryStorage,String quality){
        DryStorage dryStorageDB = dryStorageService.findById(dryStorage.getId());
        PackagedProduct product = packagedProductService.createPackageOak(dryStorageDB.getDeskOakList(),String.valueOf(dryStorageDB.getId()),dryStorage.getCodeOfProduct(),quality,
                dryStorageDB.getSizeOfHeight(),dryStorageDB.getSizeOfLong(),userId,2);
        PackagedProductDTO after = PackagedProductDTO.convertToDTO(product);
        after.setDeskOakList(DescriptionDeskOakDTO.convertToDTO(dryStorageDB.getDeskOakList()));
        loggerDataInfoService.save(breedOfTreeService.findById(2),StorageType.PACKAGE,LoggerOperationType.SENDING,null,after);
        return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+2;
    }


    @PostMapping("/editPackOak-{userId}-2")
    public String editPackOak(@PathVariable("userId")int userId,PackagedProduct product){
        int breedId=2;
        PackagedProductDTO before = PackagedProductDTO.convertToDTO(packagedProductService.findById(product.getId()));
        PackagedProduct savedAfter = packagedProductService.editPackageProductOak(product);
        PackagedProductDTO after = PackagedProductDTO.convertToDTO(savedAfter);
        loggerDataInfoService.save(breedOfTreeService.findById(2),StorageType.PACKAGE,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+breedId;
    }

    @PostMapping("/addPackOakDesc-{userId}-2")
    public String addPackOakDesc(@PathVariable("userId")int userId,String packageId, String width,String count){
        int breedId=2;
        PackagedProductDTO before = PackagedProductDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)));
        packagedProductService.addDescriptionOak(packageId,width,count);
        PackagedProductDTO after = PackagedProductDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)));
        loggerDataInfoService.save(breedOfTreeService.findById(2),StorageType.PACKAGE,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+breedId;
    }

    @PostMapping("/editPackOakDesc-{userId}-2")
    public String editPackOakDesc(@PathVariable("userId")int userId,String rowId,String packageId, String width, String count){
        int breedId=2;
        PackagedProductDTO before = PackagedProductDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)));
        deskOakService.editDescription(rowId,width,count);
        packagedProductService.editPackageProductOak(packagedProductService.findById(Integer.parseInt(packageId)));
        PackagedProductDTO after = PackagedProductDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)));
        loggerDataInfoService.save(breedOfTreeService.findById(2),StorageType.PACKAGE,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+breedId;
    }

    @PostMapping("/deletePackOakDesc-{userId}-2")
    public String deletePackOakDesc(@PathVariable("userId")int userId,String packageId, String id){
        int breedId=2;
        PackagedProductDTO before = PackagedProductDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)));
        packagedProductService.deleteDescriptionOak(packageId,id);
        PackagedProductDTO after = PackagedProductDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)));
        loggerDataInfoService.save(breedOfTreeService.findById(2),StorageType.PACKAGE,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfPackagedProduct-"+userId+"-"+breedId;
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
        model.addAttribute("deliveryDocumentations",deliveryDocumentationService.getListByUserByBreed(breedId,userId).stream().sorted((o1, o2) -> o2.getId()-o1.getId()).collect(Collectors.toList()));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("contractList",orderInfoService.getOrdersListByAgentByBreed(contrAgent.getId(),breedId));
        model.addAttribute("breedName",breedOfTreeService.findById(breedId).getBreed());
        model.addAttribute("dryStorageList",dryStorageService.getListByUserByBreed(breedId,userId));


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
        DeliveryDocumentationDTO before = DeliveryDocumentationDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)).getDeliveryDocumentation(),true);
        deskOakService.editDescription(rowId,width,count);
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(packageId));
        packagedProductService.countExtentOak(product);
        reloadAllExtentFields(product.getDeliveryDocumentation());
        DeliveryDocumentationDTO after = DeliveryDocumentationDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)).getDeliveryDocumentation(),true);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DELIVERY,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }

    @PostMapping("/addPackageDescriptionOak-{userId}-{breedId}")
    public String addPackageDescriptionOak(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,
                                            String packageId, String width, String count) {
        DeliveryDocumentationDTO before = DeliveryDocumentationDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)).getDeliveryDocumentation(),true);
        packagedProductService.addDescriptionOak(packageId,width,count);
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(packageId));
        packagedProductService.countExtentOak(product);
        reloadAllExtentFields(product.getDeliveryDocumentation());
        DeliveryDocumentationDTO after = DeliveryDocumentationDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)).getDeliveryDocumentation(),true);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DELIVERY,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }

    @PostMapping("/deletePackageDescriptionOak-{userId}-{breedId}")
    public String deletePackageDescriptionOak(@PathVariable("userId")int userId,@PathVariable("breedId")int breedId,
                                           String packageId, String id) {
        DeliveryDocumentationDTO before = DeliveryDocumentationDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)).getDeliveryDocumentation(),true);
        packagedProductService.deleteDescriptionOak(packageId,id);
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(packageId));
        packagedProductService.countExtentOak(product);
        reloadAllExtentFields(product.getDeliveryDocumentation());
        DeliveryDocumentationDTO after = DeliveryDocumentationDTO.convertToDTO(packagedProductService.findById(Integer.parseInt(packageId)).getDeliveryDocumentation(),true);
        loggerDataInfoService.save(breedOfTreeService.findById(breedId),StorageType.DELIVERY,LoggerOperationType.UPDATING,before,after);
        return "redirect:/fabric/getListOfDeliveryDocumentation-"+userId+"-"+breedId;
    }




//    Recycle page
    @PostMapping("/cutOfRecycle-{userId}-2")
    public String  cutOfRecycle(@PathVariable("userId")int userId,
                                int idOfTreeStorageRow, String extentOfTreeStorage, RawStorage rawStorage){
        int breedId =2;
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
}

