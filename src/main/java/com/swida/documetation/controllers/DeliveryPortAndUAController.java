package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.*;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.DescriptionDeskOakService;
import com.swida.documetation.data.service.storages.PackagedProductService;
import com.swida.documetation.data.service.subObjects.*;
import com.swida.documetation.utils.other.GenerateResponseForExport;
import com.swida.documetation.utils.xlsParsers.ImportOakOrderDataFromXLS;
import com.swida.documetation.utils.xlsParsers.ImportOrderDataFromXLS;
import com.swida.documetation.utils.xlsParsers.ParseOakDeliveryInfoToXLS;
import com.swida.documetation.utils.xlsParsers.ParserDeliveryDocumentationToXLS;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PseudoColumnUsage;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/multimodal")
public class DeliveryPortAndUAController {
    private UserCompanyService userCompanyService;
    private BreedOfTreeService breedOfTreeService;
    private ContrAgentService contrAgentService;
    private OrderInfoService orderInfoService;
    private ContainerService containerService;
    private DeliveryDocumentationService deliveryDocumentationService;
    private PackagedProductService packagedProductService;
    private DriverInfoService driverInfoService;
    private DescriptionDeskOakService deskOakService;

    @Autowired
    public DeliveryPortAndUAController(UserCompanyService userCompanyService, BreedOfTreeService breedOfTreeService,
                                       ContrAgentService contrAgentService, OrderInfoService orderInfoService,
                                       ContainerService containerService, DeliveryDocumentationService deliveryDocumentationService,
                                       PackagedProductService packagedProductService, DriverInfoService driverInfoService,
                                       DescriptionDeskOakService deskOakService) {
        this.userCompanyService = userCompanyService;
        this.breedOfTreeService = breedOfTreeService;
        this.contrAgentService = contrAgentService;
        this.orderInfoService = orderInfoService;
        this.containerService = containerService;
        this.deliveryDocumentationService = deliveryDocumentationService;
        this.packagedProductService = packagedProductService;
        this.driverInfoService = driverInfoService;
        this.deskOakService = deskOakService;
    }
    private void btnConfig( Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        if(hasAdminRole){
            model.addAttribute("btnConfig","btnON");
        }

    }


    // Main page
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
        model.addAttribute("distributeOrderList",orderInfoService.getOrdersByStatusOfOrderByDestinationOnlyActive(StatusOfOrderInfo.DISTRIBUTION,DeliveryDestinationType.COUNTRY));

        model.addAttribute("urlSendToArchive","/multimodal/sendToArchiveUA");

        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userCompany!=null){
            model.addAttribute("userCompanyName",userCompany);
            model.addAttribute("userId",userCompany.getId());
        }
        btnConfig(model);
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
        model.addAttribute("distributeOrderList",orderInfoService.getOrdersByStatusOfOrderByDestinationOnlyActive(StatusOfOrderInfo.DISTRIBUTION,DeliveryDestinationType.PORT));

        model.addAttribute("urlSendToArchive","/multimodal/sendToArchivePort");

        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userCompany!=null){
            model.addAttribute("userCompanyName",userCompany);
            model.addAttribute("userId",userCompany.getId());
        }
        btnConfig(model);
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
        model.addAttribute("contractList",orderInfoService.getOrdersByStatusOfOrderByDestinationOnlyActive(StatusOfOrderInfo.DISTRIBUTION,mainOrder.getDestinationType()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());

        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userCompany!=null){
            model.addAttribute("userCompanyName",userCompany);
            model.addAttribute("userId",userCompany.getId());
        }
        if (breedOfTree.getId()!=2){
            model.addAttribute("fragmentPathDeliveryDoc","deliveryInfo");
        }else{
            model.addAttribute("breedId",2);
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

        model.addAttribute("urlEditPackageDescriptionOak","/multimodal/editPackageDescriptionOak-"+contractId);
        model.addAttribute("urlAddPackageDescriptionOak","/multimodal/addPackageDescriptionOak-"+contractId);
        model.addAttribute("urlDeleteDescriptionOak","/multimodal/deletePackageDescriptionOak-"+contractId);
        btnConfig(model);


        return "multimodalPage";
    }

//    Work with Delivery doc

    public void reloadAllExtentFields(DeliveryDocumentation deliveryDocumentation ){
        deliveryDocumentationService.reloadExtentOfAllPack(deliveryDocumentation);
        List<Integer> list = new ArrayList<>();
        list.add(deliveryDocumentation.getOrderInfo().getId());
        List<DeliveryDocumentation> docList = deliveryDocumentationService.getListByDistributionContractsId(list);
        orderInfoService.reloadOrderExtent(deliveryDocumentation.getOrderInfo(),docList);
        orderInfoService.reloadMainOrderExtent(deliveryDocumentation.getOrderInfo().getMainOrder());
    }

    @PostMapping("/editDeliveryDocumentation-{contractId}")
    public String editDeliveryDocumentation(@PathVariable("contractId")int contractId,DeliveryDocumentation documentation){
        DeliveryDocumentation docDB = deliveryDocumentationService.editDeliveryDoc(documentation);
        reloadAllExtentFields(docDB);
        return "redirect:/multimodal/getDeliveryTrucksByContract-"+contractId;
    }

    @PostMapping("/editPackageProduct-{contractId}")
    public String editPackageProduct(@PathVariable("contractId")int contractId, PackagedProduct product){
        PackagedProduct productDB = packagedProductService.editPackageProduct(product);
        packagedProductService.countExtentOak(productDB);
        reloadAllExtentFields(productDB.getDeliveryDocumentation());
        return "redirect:/multimodal/getDeliveryTrucksByContract-"+contractId;
    }
    @PostMapping("/addPackageProduct-{contractId}")
    public String addPackageProduct(@PathVariable("contractId")int contractId,String docId,PackagedProduct product){
        deliveryDocumentationService.addPackageProductToDeliveryDoc(docId,product);
        reloadAllExtentFields(product.getDeliveryDocumentation());
        return "redirect:/multimodal/getDeliveryTrucksByContract-"+contractId;
    }
    @PostMapping("/deletePackageProduct-{contractId}")
    public String deletePackageProduct(@PathVariable("contractId")int contractId,String id, String deliveryId){
        deliveryDocumentationService.deletePackage(id,deliveryId);
        DeliveryDocumentation documentation =  deliveryDocumentationService.findById(Integer.parseInt(deliveryId));
        reloadAllExtentFields(documentation);

        return "redirect:/multimodal/getDeliveryTrucksByContract-"+contractId;
    }

    //    Work with Delivery doc OAK

    @PostMapping("/editPackageDescriptionOak-{contractId}")
    public String editPackageDescriptionOak(@PathVariable("contractId")int contractId,
                                            String rowId,String packageId, String width, String count) {
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(packageId));
        deskOakService.editDescription(rowId,width,count);
        packagedProductService.countExtentOak(product);
        reloadAllExtentFields(product.getDeliveryDocumentation());
        return "redirect:/multimodal/getDeliveryTrucksByContract-"+contractId;
    }

    @PostMapping("/addPackageDescriptionOak-{contractId}")
    public String addPackageDescriptionOak(@PathVariable("contractId")int contractId,
                                           String packageId, String width, String count) {
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(packageId));
        packagedProductService.addDescriptionOak(packageId,width,count);
        packagedProductService.countExtentOak(product);
        reloadAllExtentFields(product.getDeliveryDocumentation());
        return "redirect:/multimodal/getDeliveryTrucksByContract-"+contractId;
    }

    @PostMapping("/deletePackageDescriptionOak-{contractId}")
    public String deletePackageDescriptionOak(@PathVariable("contractId")int contractId,
                                              String packageId, String id) {
        PackagedProduct product = packagedProductService.findById(Integer.parseInt(packageId));

        packagedProductService.deleteDescriptionOak(packageId,id);
        packagedProductService.countExtentOak(product);
        reloadAllExtentFields(product.getDeliveryDocumentation());
        return "redirect:/multimodal/getDeliveryTrucksByContract-"+contractId;
    }


    @PostMapping("/importOakXLS")
    public String importOakXLS(@RequestParam MultipartFile fileXLS, String contractId) throws IOException, InvalidFormatException {
        OrderInfo orderInfo = orderInfoService.findById(Integer.parseInt(contractId));
        if (orderInfo.getBreedOfTree().getId()==2) {
            ImportOakOrderDataFromXLS dataFromXLS = new ImportOakOrderDataFromXLS(fileXLS);
            System.out.println("contractId " + contractId);
            deliveryDocumentationService.checkInfoFromImportOak(dataFromXLS.importData(),orderInfo);
        }else{
            ImportOrderDataFromXLS dataFromXLS = new ImportOrderDataFromXLS(fileXLS);
            deliveryDocumentationService.checkInfoFromImport(dataFromXLS.importData(),orderInfo);
        }

        List<Integer> list = new ArrayList<>();
        list.add(orderInfo.getId());
        List<DeliveryDocumentation> docList = deliveryDocumentationService.getListByDistributionContractsId(list);
        orderInfoService.reloadOrderExtent(orderInfo,docList);
        orderInfoService.reloadMainOrderExtent(orderInfo.getMainOrder());

        return "redirect:/multimodal/getDeliveryPort";
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

    @PostMapping("/sendToArchiveUA")
    public String sendToArchiveUA(String id){
        OrderInfo mainOrder = orderInfoService.findById(Integer.parseInt(id));
        if (mainOrder.getStatusOfEntity()==StatusOfEntity.ARCHIVED){
            return "redirect:/multimodal/getDeliveryInUkraine";
        }
        archiveContractInfo(mainOrder);
        return "redirect:/multimodal/getDeliveryInUkraine";
    }

    @PostMapping("/sendToArchivePort")
    public String sendToArchivePort(String id){
        OrderInfo mainOrder = orderInfoService.findById(Integer.parseInt(id));
        if (mainOrder.getStatusOfEntity()==StatusOfEntity.ARCHIVED){
            return "redirect:/multimodal/getDeliveryPort";
        }
        archiveContractInfo(mainOrder);
        return "redirect:/multimodal/getDeliveryPort";
    }


    private void archiveContractInfo( OrderInfo mainOrder){
        mainOrder.setStatusOfEntity(StatusOfEntity.ARCHIVED);
        List<OrderInfo> distributedOrder = orderInfoService.findDistributionObj(mainOrder.getId());
        orderInfoService.save(mainOrder);

        for(OrderInfo order:distributedOrder){
            order.setStatusOfEntity(StatusOfEntity.ARCHIVED);
            orderInfoService.save(order);
        }
        List<DeliveryDocumentation> docList = deliveryDocumentationService.getListByDistributionContractsId(orderInfoService.findDistributionId(mainOrder.getId()));
        for(DeliveryDocumentation doc:docList){
            doc.setStatusOfEntity(StatusOfEntity.ARCHIVED);
            deliveryDocumentationService.save(doc);
            for(PackagedProduct product:doc.getProductList()){
                product.setStatusOfEntity(StatusOfEntity.ARCHIVED);
                product.setStatusOfProduct(StatusOfProduct.DELIVERED);
                packagedProductService.save(product);
            }
        }
    }
}
