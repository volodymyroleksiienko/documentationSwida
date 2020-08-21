package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.ContrAgentType;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfOrderInfo;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.PackagedProductService;
import com.swida.documetation.data.service.subObjects.*;
import com.swida.documetation.utils.xlsParsers.ImportOakOrderDataFromXLS;
import com.swida.documetation.utils.xlsParsers.ImportOrderDataFromXLS;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/multimodal")
public class DeliveryUAController {
    private UserCompanyService userCompanyService;
    private BreedOfTreeService breedOfTreeService;
    private ContrAgentService contrAgentService;
    private OrderInfoService orderInfoService;
    private ContainerService containerService;
    private DeliveryDocumentationService deliveryDocumentationService;
    private PackagedProductService packagedProductService;
    private DriverInfoService driverInfoService;

    @Autowired
    public DeliveryUAController(UserCompanyService userCompanyService, BreedOfTreeService breedOfTreeService,
                                ContrAgentService contrAgentService, OrderInfoService orderInfoService,
                                ContainerService containerService,DeliveryDocumentationService deliveryDocumentationService,
                                PackagedProductService packagedProductService, DriverInfoService driverInfoService) {
        this.userCompanyService = userCompanyService;
        this.breedOfTreeService = breedOfTreeService;
        this.contrAgentService = contrAgentService;
        this.orderInfoService = orderInfoService;
        this.containerService = containerService;
        this.deliveryDocumentationService = deliveryDocumentationService;
        this.packagedProductService = packagedProductService;
        this.driverInfoService = driverInfoService;
    }
    // Main page
//    @GetMapping("/getDeliveryInUkraine-{breedId}")
//    public String getDeliveryInUkraine(@PathVariable("breedId") int breedId, Model model){
//
//        model.addAttribute("navTabName","multimodalMain");
//        model.addAttribute("tabName",breedId);
//        model.addAttribute("fragmentPathTabConfig","transportationTab");
//        model.addAttribute("fragmentPathDeliveryUA", (breedId==2)?"deliveryInfoOak":"deliveryInfo");
//        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
//        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
//        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
//        model.addAttribute("navTabName","delivery");
//        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
//        if (userCompany!=null){
//            model.addAttribute("userCompanyName",userCompany);
//            model.addAttribute("userId",userCompany.getId());
//        }
//        List<DeliveryDocumentation> list = deliveryDocumentationService.getListByDestinationType(DeliveryDestinationType.COUNTRY);
//
//        list.removeIf(doc -> doc.getBreedOfTree().getId() != breedId);
//
//        model.addAttribute("deliveryDocumentations",list);
//        return "multimodalPage";
//    }

    @GetMapping("/getDeliveryInUkraine")
    public String getDeliveryInUkraine(Model model){

        model.addAttribute("navTabName","delivery");
        model.addAttribute("tabName","contracts");
        model.addAttribute("fragmentPathTabConfig","deliveryUA");
        model.addAttribute("fragmentPathOrderInfo", "ordersForDelivery");
        model.addAttribute("orderInfoList",orderInfoService.getOrdersByStatusOfOrderByDestination(StatusOfOrderInfo.MAIN, DeliveryDestinationType.COUNTRY));
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));


        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userCompany!=null){
            model.addAttribute("userCompanyName",userCompany);
            model.addAttribute("userId",userCompany.getId());
        }
        return "multimodalPage";
    }

    @GetMapping("/getDeliveryPort")
    public String getDeliveryPort(Model model){

        model.addAttribute("navTabName","delivery");
        model.addAttribute("tabName","contracts");
        model.addAttribute("fragmentPathTabConfig","deliveryUA");
        model.addAttribute("fragmentPathOrderInfo", "ordersForDelivery");
        model.addAttribute("orderInfoList",orderInfoService.getOrdersByStatusOfOrderByDestination(StatusOfOrderInfo.MAIN, DeliveryDestinationType.PORT));
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));


        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userCompany!=null){
            model.addAttribute("userCompanyName",userCompany);
            model.addAttribute("userId",userCompany.getId());
        }
        return "multimodalPage";
    }

    @GetMapping("/getDeliveryTrucksByContract-{id}")
    public String getDeliveryTrucksByContract(@PathVariable("id")int contractId,Model model){
        OrderInfo mainOrder = orderInfoService.findById(contractId);
        BreedOfTree breedOfTree = mainOrder.getBreedOfTree();
        List<DeliveryDocumentation> docList = deliveryDocumentationService.getListByDistributionContractsId(orderInfoService.findDistributionId(contractId));

        model.addAttribute("navTabName","delivery");
        model.addAttribute("tabName","details");
        model.addAttribute("fragmentPathTabConfig","deliveryUA");
        model.addAttribute("deliveryDocumentations",docList);
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userCompany!=null){
            model.addAttribute("userCompanyName",userCompany);
            model.addAttribute("userId",userCompany.getId());
        }
        if (breedOfTree.getId()!=2){
            model.addAttribute("fragmentPathDeliveryDoc","deliveryInfo");
        }else{
            model.addAttribute("fragmentPathDeliveryDoc","deliveryInfoOak");
        }
        if(orderInfoService.findById(contractId).getDestinationType()==DeliveryDestinationType.COUNTRY){
            model.addAttribute("urlContractPage","/multimodal/getDeliveryInUkraine");
        }
        if(orderInfoService.findById(contractId).getDestinationType()==DeliveryDestinationType.PORT){
            model.addAttribute("urlContractPage","/multimodal/getDeliveryPort");
        }

        model.addAttribute("urlEditDriver","/multimodal/editDeliveryDocumentation-"+contractId);
        model.addAttribute("urlEditPackage","/multimodal/editPackageProduct-"+contractId);
        model.addAttribute("urlAddPackage","/multimodal/addPackageProduct-"+contractId);
        model.addAttribute("urlDeletePackage","/multimodal/deletePackageProduct-"+contractId);

        return "multimodalPage";
    }

    @PostMapping("/editDeliveryDocumentation-{contractId}")
    public String editDeliveryDocumentation(@PathVariable("contractId")int contractId,DeliveryDocumentation documentation){
        deliveryDocumentationService.editDeliveryDoc(documentation);
        return "redirect:/multimodal/getDeliveryTrucksByContract-"+contractId;
    }

    @PostMapping("/editPackageProduct-{contractId}")
    public String editPackageProduct(@PathVariable("contractId")int contractId, PackagedProduct product){
        packagedProductService.editPackageProduct(product);
        return "redirect:/multimodal/getDeliveryTrucksByContract-"+contractId;
    }
    @PostMapping("/addPackageProduct-{contractId}")
    public String addPackageProduct(@PathVariable("contractId")int contractId,String docId,PackagedProduct product){
        deliveryDocumentationService.addPackageProductToDeliveryDoc(docId,product);
        return "redirect:/multimodal/getDeliveryTrucksByContract-"+contractId;
    }
    @PostMapping("/deletePackageProduct-{contractId}")
    public String deletePackageProduct(@PathVariable("contractId")int contractId,String id, String deliveryId){
        deliveryDocumentationService.deletePackage(id,deliveryId);
        return "redirect:/multimodal/getDeliveryTrucksByContract-"+contractId;
    }

    @PostMapping("/importOakXLS")
    public String importOakXLS(@RequestParam MultipartFile fileXLS, String contractId) throws IOException, InvalidFormatException {
        ImportOakOrderDataFromXLS dataFromXLS = new ImportOakOrderDataFromXLS(fileXLS);
        System.out.println("contractId "+ contractId);
        dataFromXLS.importData();

//        OrderInfo orderInfo = orderInfoService.findByCodeOfOrder(contractId);

//        deliveryDocumentationService.checkInfoFromImport(dataFromXLS.importData(),orderInfo);
        return "redirect:/multimodal/getDeliveryPort";
    }
}
