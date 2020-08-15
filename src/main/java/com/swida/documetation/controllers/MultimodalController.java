package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.Container;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.ContrAgentType;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfOrderInfo;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.PackagedProductService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.ContainerService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import com.swida.documetation.utils.other.GenerateResponseForExport;
import com.swida.documetation.utils.xlsParsers.ImportOrderDataFromXLS;
import com.swida.documetation.utils.xlsParsers.ParseMultimodalPackagesByContract;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/multimodal")
public class MultimodalController {
    private UserCompanyService userCompanyService;
    private BreedOfTreeService breedOfTreeService;
    private ContrAgentService contrAgentService;
    private OrderInfoService orderInfoService;
    private ContainerService containerService;
    private DeliveryDocumentationService deliveryDocumentationService;
    private PackagedProductService packagedProductService;

    @Autowired
    public MultimodalController(UserCompanyService userCompanyService, BreedOfTreeService breedOfTreeService,
                                ContrAgentService contrAgentService, OrderInfoService orderInfoService,
                                ContainerService containerService,DeliveryDocumentationService deliveryDocumentationService,
                                PackagedProductService packagedProductService) {
        this.userCompanyService = userCompanyService;
        this.breedOfTreeService = breedOfTreeService;
        this.contrAgentService = contrAgentService;
        this.orderInfoService = orderInfoService;
        this.containerService = containerService;
        this.deliveryDocumentationService = deliveryDocumentationService;
        this.packagedProductService = packagedProductService;
    }
// Main page
    @GetMapping("/getDeliveryInUkraine-{breedId}")
    public String getDeliveryInUkraine(@PathVariable("breedId") int breedId,  Model model){

        model.addAttribute("navTabName","multimodalMain");
        model.addAttribute("tabName",breedId);
        model.addAttribute("fragmentPathTabConfig","transportationTab");
        model.addAttribute("fragmentPathDeliveryUA", (breedId==2)?"deliveryInfoOak":"deliveryInfo");
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        List<DeliveryDocumentation> list = deliveryDocumentationService.getListByDestinationType(DeliveryDestinationType.COUNTRY);

        list.removeIf(doc -> doc.getBreedOfTree().getId() != breedId);

        model.addAttribute("deliveryDocumentations",list);
        return "multimodalPage";
    }



    @GetMapping("/getMultimodalOrders")
    public String getMultimodalOrders(Model model){

        List<ContrAgent> contrAgentList = new ArrayList<>();
        contrAgentList.addAll(contrAgentService.getListByType(ContrAgentType.BUYER_UA));
        contrAgentList.addAll(contrAgentService.getListByType(ContrAgentType.BUYER_FOREIGN));

        model.addAttribute("navTabName","multimodalMain");
        model.addAttribute("tabName","contracts");
        model.addAttribute("fragmentPathMultimodalMain", "multimodalContracts");
        model.addAttribute("fragmentPathTabConfig","multimodalMain");
        model.addAttribute("multimodalOrderList",orderInfoService.getOrdersByStatusOfOrder(StatusOfOrderInfo.MAIN));
        model.addAttribute("distributeOrderList",orderInfoService.getOrdersByStatusOfOrder(StatusOfOrderInfo.DISTRIBUTION));
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("contrAgentList",contrAgentList);
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));

        return "multimodalPage";
    }

    @PostMapping("/editMultimodalContarct")
    public String editContarct(OrderInfo orderInfo){
        OrderInfo orderInfoDB = orderInfoService.findById(orderInfo.getId());
        float differenceExtent = Float.parseFloat(orderInfoDB.getExtentOfOrder())-Float.parseFloat(orderInfo.getExtentOfOrder());


        orderInfoDB.setExtentOfOrder(orderInfo.getExtentOfOrder());
        orderInfoDB.setBreedDescription(orderInfo.getBreedDescription());
        orderInfoDB.setDoneExtendOfOrder(orderInfo.getDoneExtendOfOrder());
        orderInfoDB.setCodeOfOrder(orderInfo.getCodeOfOrder());
        orderInfoDB.setContrAgent(contrAgentService.getObjectByName(orderInfo.getContrAgent().getNameOfAgent()));
        orderInfoDB.setStatusOfOrderInfo(StatusOfOrderInfo.MAIN);
        orderInfoDB.setBreedOfTree(breedOfTreeService.getObjectByName(orderInfo.getBreedOfTree().getBreed()));
        orderInfoDB.setExtentForDistribution(String.format("%.3f",Float.parseFloat(orderInfoDB.getExtentForDistribution())-differenceExtent).replace(",","."));
        orderInfoDB.setDate(orderInfo.getDate());

        orderInfoService.save(orderInfoDB);
        return "redirect:/multimodal/getMultimodalOrders";
    }

    @GetMapping("/exportAllPackagesByContract-{id}")
    public ResponseEntity<Resource> exportAllPackagesByContract(@PathVariable("id")int orderId) throws FileNotFoundException {
        List<DeliveryDocumentation> deliveryDocumentation = deliveryDocumentationService
                .getListByDistributionContractsId(orderInfoService.findDistributionId(orderId));
        ParseMultimodalPackagesByContract parser = new ParseMultimodalPackagesByContract(deliveryDocumentation);
        String filePath = parser.parse();

        return new GenerateResponseForExport().generate(filePath,orderInfoService.findById(orderId).getCodeOfOrder(),"");
    }

    @PostMapping("/importXLS")
    public String importXLS(@RequestParam MultipartFile fileXLS, String contractId) throws IOException, InvalidFormatException {
        ImportOrderDataFromXLS dataFromXLS = new ImportOrderDataFromXLS(fileXLS);
        System.out.println("contractId "+ contractId);
        OrderInfo orderInfo = orderInfoService.findByCodeOfOrder(contractId);

        deliveryDocumentationService.checkInfoFromImport(dataFromXLS.importData(),orderInfo);
        return "redirect:/multimodal/getMultimodalOrders";
    }



//    All Containers
    @GetMapping("/getListOfALLContainers")
    public String getListOfALLContainers(Model model){

        model.addAttribute("navTabName","multimodalMain");
        model.addAttribute("tabName","containers");
        model.addAttribute("fragmentPathMultimodalContainers", "multimodalContainers");
        model.addAttribute("fragmentPathTabConfig","multimodalMain");
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("containerList",containerService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());

        return "multimodalPage";
    }

    @PostMapping("/addContainerMultimodal")
    public String addContainerMultimodal(Container container){
        containerService.save(container);
        return "redirect:/multimodal/getListOfALLContainers";
    }

    @PostMapping("/editContainerMultimodal")
    public String editContainerMultimodal(Container container){
        containerService.save(container);
        return "redirect:/multimodal/getListOfALLContainers";
    }

//    Details of contract page
    @GetMapping("/getTrucksByContract-{id}")
    public String getDetailsOfContract(@PathVariable("id")int contractId, Model model){
        model.addAttribute("navTabName","multimodalDetails");
        model.addAttribute("tabName","trucks");
        model.addAttribute("contractId",contractId);
        model.addAttribute("fragmentPathMultimodalTrucks", "multimodalTrucks");
        model.addAttribute("fragmentPathTabConfig","multimodalDetails");
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("containerList",containerService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("deliveryDocumentationList",deliveryDocumentationService
                .getListByDistributionContractsId(orderInfoService.findDistributionId(contractId)));
        System.out.println(deliveryDocumentationService
                .getListByDistributionContractsId(orderInfoService.findDistributionId(contractId)));
        return "multimodalPage";
    }


//    Full Details Page
    @GetMapping("/getFullDetailsOfContract-{id}")
    public String getFullDetailsOfContract(@PathVariable("id")int contractId,Model model){

        List<DeliveryDocumentation> deliveryDocumentation = deliveryDocumentationService
                .getListByDistributionContractsId(orderInfoService.findDistributionId(contractId));

        model.addAttribute("navTabName","multimodalFullDetails");
        model.addAttribute("fragmentPathTabConfig","multimodalFullDetails");
        model.addAttribute("fragmentPathMultimodalFullDetails","multimodalFullDetails");
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("contractPackagesList",deliveryDocumentation);
        model.addAttribute("contractId",contractId);

        model.addAttribute(" ",deliveryDocumentationService.getAllTruckIdList(deliveryDocumentation));
        return "multimodalPage";
    }

    @PostMapping("/addPackageToDeliveryDoc-{id}")
    public String addPackageToDeliveryDoc(@PathVariable("id")int contractId, PackagedProduct packagedProduct,
                                          String contractName, String containerName, String driverIdOfTruck){

        OrderInfo orderInfo = orderInfoService.findByCodeOfOrder(contractName);
        DeliveryDocumentation doc = deliveryDocumentationService.getDeliveryDocumentationByIdOfTruck(driverIdOfTruck);

        packagedProduct.setOrderInfo(orderInfo);
        packagedProductService.save(packagedProduct);
        doc.getProductList().add(packagedProduct);
        deliveryDocumentationService.save(doc);

        return "redirect:/multimodal/getFullDetailsOfContract-"+contractId;
    }

}
