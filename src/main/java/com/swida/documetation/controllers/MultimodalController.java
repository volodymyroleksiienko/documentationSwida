package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.Container;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.entity.subObjects.DriverInfo;
import com.swida.documetation.data.enums.*;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.PackagedProductService;
import com.swida.documetation.data.service.subObjects.*;
import com.swida.documetation.utils.other.GenerateResponseForExport;
import com.swida.documetation.utils.xlsParsers.ImportOrderDataFromXLS;
import com.swida.documetation.utils.xlsParsers.ParseMultimodalPackagesByContract;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private DriverInfoService driverInfoService;

    @Autowired
    public MultimodalController(UserCompanyService userCompanyService, BreedOfTreeService breedOfTreeService,
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

    @GetMapping("/getMultimodalOrders")
    public String getMultimodalOrders(Model model){

        List<ContrAgent> contrAgentList = new ArrayList<>();
        contrAgentList.addAll(contrAgentService.getListByType(ContrAgentType.BUYER_UA));
        contrAgentList.addAll(contrAgentService.getListByType(ContrAgentType.BUYER_FOREIGN));

        model.addAttribute("navTabName","multimodalMain");
        model.addAttribute("tabName","contracts");
        model.addAttribute("fragmentPathMultimodalMain", "multimodalContracts");
        model.addAttribute("fragmentPathTabConfig","multimodalMain");
        model.addAttribute("multimodalOrderList",orderInfoService.getOrdersByStatusOfOrderByDestination(StatusOfOrderInfo.MAIN, DeliveryDestinationType.MULTIMODAL));
        model.addAttribute("distributeOrderList",orderInfoService.getOrdersByStatusOfOrderByDestinationOnlyActive(StatusOfOrderInfo.DISTRIBUTION,DeliveryDestinationType.MULTIMODAL));
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("contrAgentList",contrAgentList);
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("navTabName","delivery");

        model.addAttribute("buttonConfig","multimodalMain");

        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userCompany!=null){
            model.addAttribute("userCompanyName",userCompany);
            model.addAttribute("userId",userCompany.getId());
        }
        return "multimodalPage";
    }

    @GetMapping("/getLeftOverInfo")
    public String getLeftOverInfo(Model model){

        List<ContrAgent> contrAgentList = new ArrayList<>();
        contrAgentList.addAll(contrAgentService.getListByType(ContrAgentType.BUYER_UA));
        contrAgentList.addAll(contrAgentService.getListByType(ContrAgentType.BUYER_FOREIGN));
        List<OrderInfo> multimodalOrders = orderInfoService.getOrdersByStatusOfOrderByDestination(StatusOfOrderInfo.LEFT_OVER, DeliveryDestinationType.MULTIMODAL);

        orderInfoService.deleteEmptyLeftOverOrders(multimodalOrders);

        model.addAttribute("navTabName","multimodalMain");
        model.addAttribute("tabName","leftOver");
        model.addAttribute("fragmentPathMultimodalMain", "multimodalContracts");
        model.addAttribute("fragmentPathTabConfig","multimodalMain");
        model.addAttribute("multimodalOrderList",orderInfoService.getOrdersByStatusOfOrderByDestination(StatusOfOrderInfo.LEFT_OVER, DeliveryDestinationType.MULTIMODAL));
        model.addAttribute("distributeOrderList",orderInfoService.getOrdersByStatusOfOrder(StatusOfOrderInfo.DISTRIBUTION));
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("contrAgentList",contrAgentList);
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("navTabName","delivery");
        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userCompany!=null){
            model.addAttribute("userCompanyName",userCompany);
            model.addAttribute("userId",userCompany.getId());
        }
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
        OrderInfo orderInfo = orderInfoService.findById(Integer.parseInt(contractId));

        deliveryDocumentationService.checkInfoFromImport(dataFromXLS.importData(),orderInfo);
        reloadOrdersExtent(orderInfo);
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
        model.addAttribute("navTabName","delivery");
        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userCompany!=null){
            model.addAttribute("userCompanyName",userCompany);
            model.addAttribute("userId",userCompany.getId());
        }
        return "multimodalPage";
    }

    @PostMapping("/archiveContainer")
    public String archiveContainer(String id){
        Container container = containerService.findById(Integer.parseInt(id));
        container.setStatusOfEntity(StatusOfEntity.ARCHIVED);
        containerService.save(container);
        return "redirect:/multimodal/getListOfALLContainers";
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
        model.addAttribute("tabName","trucks");
        model.addAttribute("contractId",contractId);
        model.addAttribute("fragmentPathMultimodalTrucks", "multimodalTrucks");
        model.addAttribute("fragmentPathTabConfig","multimodalDetails");
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
//        model.addAttribute("containerList",containerService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("deliveryDocumentationList",deliveryDocumentationService
                .getListByDistributionContractsId(orderInfoService.findDistributionId(contractId)));

        model.addAttribute("providerList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("navTabName","delivery");
        model.addAttribute("contractDistributionList",orderInfoService.getOrdersByStatusOfOrderByDestinationOnlyActive(StatusOfOrderInfo.DISTRIBUTION,DeliveryDestinationType.MULTIMODAL));
        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userCompany!=null){
            model.addAttribute("userCompanyName",userCompany);
            model.addAttribute("userId",userCompany.getId());
        }
        return "multimodalPage";
    }

    @PostMapping("/addTrucksByContract-{id}")
    public String addTrucksByContract(@PathVariable("id")int contractId, DeliveryDocumentation docWeb){

        DriverInfo driverInfo = new DriverInfo();
        driverInfo.setIdOfTruck(docWeb.getDriverInfo().getIdOfTruck());
        driverInfoService.save(driverInfo);

        docWeb.setDriverInfo(driverInfo);

        deliveryDocumentationService.save(docWeb);

        return "redirect:/multimodal/getTrucksByContract-"+contractId;
    }

    @PostMapping("/editTrucksByContract-{id}")
    public String editTrucksByContract(@PathVariable("id")int contractId, DeliveryDocumentation docWeb){
        DeliveryDocumentation docDB = deliveryDocumentationService.findById(docWeb.getId());
        OrderInfo oldOrder = docDB.getOrderInfo();
        docDB.setDateOfUnloading(docWeb.getDateOfUnloading());
        docDB.getDriverInfo().setIdOfTruck(docWeb.getDriverInfo().getIdOfTruck());
        docDB.setContrAgent(contrAgentService.findById(docWeb.getContrAgent().getId()));
        docDB.setOrderInfo(orderInfoService.findById(docWeb.getOrderInfo().getId()));
        docDB.setPackagesExtent(docWeb.getPackagesExtent());
        deliveryDocumentationService.save(docDB);
        reloadAllExtentFields(docDB);
        reloadOrdersExtent(oldOrder);
        return "redirect:/multimodal/getTrucksByContract-"+contractId;
    }

//    no button
    @PostMapping("/deleteTruckByContract-{id}")
    public String deleteTruckByContract(@PathVariable("id")int contractId, String id){
        DeliveryDocumentation doc = deliveryDocumentationService.findById(Integer.parseInt(id));
        List<PackagedProduct> products = doc.getProductList();

        driverInfoService.deleteByID(doc.getDriverInfo().getId());
        for(PackagedProduct product :products){
            packagedProductService.deleteByID(product.getId());
        }
        deliveryDocumentationService.deleteByID(doc.getId());

        return "redirect:/multimodal/getTrucksByContract-"+contractId;
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
        model.addAttribute("distributeOrderList",orderInfoService.findDistributionObj(contractId));
        model.addAttribute("navTabName","delivery");
        model.addAttribute("containersList",containerService.selectByStatusOfEntity(StatusOfEntity.ACTIVE));

        if(orderInfoService.findById(contractId).getStatusOfOrderInfo()!=StatusOfOrderInfo.LEFT_OVER){
            model.addAttribute("buttonConfig","multimodalMain");
        }

        UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (userCompany!=null){
            model.addAttribute("userCompanyName",userCompany);
            model.addAttribute("userId",userCompany.getId());
        }
        return "multimodalPage";
    }

    public void reloadAllExtentFields(DeliveryDocumentation deliveryDocumentation ){
        deliveryDocumentationService.reloadExtentOfAllPack(deliveryDocumentation);
        List<Integer> list = new ArrayList<>();
        list.add(deliveryDocumentation.getOrderInfo().getId());
        List<DeliveryDocumentation> docList = deliveryDocumentationService.getListByDistributionContractsId(list);
        orderInfoService.reloadOrderExtent(deliveryDocumentation.getOrderInfo(),docList);
        orderInfoService.reloadMainOrderExtent(deliveryDocumentation.getOrderInfo().getMainOrder());
    }

    public void reloadOrdersExtent(OrderInfo orderInfo){
        List<Integer> list = new ArrayList<>();
        list.add(orderInfo.getId());
        List<DeliveryDocumentation> docList = deliveryDocumentationService.getListByDistributionContractsId(list);
        orderInfoService.reloadOrderExtent(orderInfo,docList);
        orderInfoService.reloadMainOrderExtent(orderInfo.getMainOrder());
    }

    @PostMapping("/addPackageToDeliveryDoc-{contractId}")
    public String addPackageToDeliveryDoc(@PathVariable("contractId")int contractId, PackagedProduct packagedProduct,
                                          String contractName, String containerName, String driverIdOfTruck){
        if(!containerName.isEmpty()) {
            Container container = containerService.findById(Integer.parseInt(containerName));
            packagedProduct.setContainer(container);
        }
        if(!contractName.isEmpty()){
            OrderInfo orderInfo = orderInfoService.findById(Integer.parseInt(contractName));
            packagedProduct.setOrderInfo(orderInfo);
        }
        DeliveryDocumentation doc = deliveryDocumentationService.findById(Integer.parseInt(driverIdOfTruck));

        packagedProduct.setDeliveryDocumentation(doc);
        packagedProductService.save(packagedProduct);
        doc.getProductList().add(packagedProduct);
        deliveryDocumentationService.save(doc);
        reloadAllExtentFields(doc);
        return "redirect:/multimodal/getFullDetailsOfContract-"+contractId;
    }

    @PostMapping("/editPackageToDeliveryDoc-{id}")
    public String editPackageToDeliveryDoc(@PathVariable("id")int contractId, PackagedProduct packagedProduct,
                                          String contractName, String containerName, String driverIdOfTruck){
        PackagedProduct productDB = packagedProductService.editPackageProduct(packagedProduct);
        productDB.setExtent(packagedProductService.countExtent(packagedProduct));
        if(!containerName.isEmpty()){
            Container container = containerService.findById(Integer.parseInt(containerName));
            productDB.setContainer(container);
        }
        packagedProductService.save(productDB);
        reloadAllExtentFields(packagedProductService.findById(packagedProduct.getId()).getDeliveryDocumentation());
        return "redirect:/multimodal/getFullDetailsOfContract-"+contractId;
    }

    @PostMapping("/deletePackage-{contractId}")
    public String deletePackage(@PathVariable("contractId")int contractId, String id){
        DeliveryDocumentation documentation = packagedProductService.findById(Integer.parseInt(id)).getDeliveryDocumentation();
        packagedProductService.deleteByID(Integer.parseInt(id));
        reloadAllExtentFields(documentation);
        return "redirect:/multimodal/getFullDetailsOfContract-"+contractId;
    }

    @PostMapping("/sendToArchive")
    public String sendToArchive(String id){
        OrderInfo mainOrder = orderInfoService.findById(Integer.parseInt(id));
        if (mainOrder.getStatusOfEntity()==StatusOfEntity.ARCHIVED){
            return "redirect:/multimodal/getMultimodalOrders";
        }
        mainOrder.setStatusOfEntity(StatusOfEntity.ARCHIVED);
        List<OrderInfo> distributedOrder = orderInfoService.findDistributionObj(Integer.parseInt(id));

        OrderInfo leftOrder = new OrderInfo();
        OrderInfo distOrder = new OrderInfo();
        leftOrder.setCodeOfOrder(mainOrder.getCodeOfOrder()+"-остаток");
        leftOrder.setStatusOfOrderInfo(StatusOfOrderInfo.LEFT_OVER);
        leftOrder.setDestinationType(DeliveryDestinationType.MULTIMODAL);
        leftOrder.setBreedOfTree(mainOrder.getBreedOfTree());

        distOrder.setCodeOfOrder(mainOrder.getCodeOfOrder()+"-ост");
        distOrder.setBreedOfTree(mainOrder.getBreedOfTree());
        distOrder.setMainOrder(leftOrder);
        distOrder.setStatusOfOrderInfo(StatusOfOrderInfo.LEFT_OVER_DISTRIBUTION);
        distOrder.setDestinationType(DeliveryDestinationType.MULTIMODAL);

        orderInfoService.save(leftOrder);
        orderInfoService.save(distOrder);

        List<DeliveryDocumentation> docList = deliveryDocumentationService.getListByDistributionContractsId(
                orderInfoService.findDistributionId(Integer.parseInt(id)));

        for(DeliveryDocumentation documentation: docList){
            DeliveryDocumentation newDoc = createDeliveryDocLeftOver(documentation,distOrder);
            if(newDoc!=null){
                deliveryDocumentationService.reloadExtentOfAllPack(newDoc);
            }else{
                continue;
            }
            if (documentation.getProductList().size()==0){
//                deliveryDocumentationService.deleteByID(documentation.getId());
//                return null;
            }
        }

        reloadOrdersExtent(distOrder);
        for(OrderInfo order:distributedOrder){
            order.setStatusOfEntity(StatusOfEntity.ARCHIVED);
            orderInfoService.save(order);
        }

        for(DeliveryDocumentation documentation: docList){
            documentation.setStatusOfEntity(StatusOfEntity.ARCHIVED);
            reloadAllExtentFields(documentation);
        }
        return "redirect:/multimodal/getMultimodalOrders";
    }



    public DeliveryDocumentation createDeliveryDocLeftOver(DeliveryDocumentation main, OrderInfo orderInfo){
        List<PackagedProduct> packWithoutContainer = new ArrayList<>();
        List<PackagedProduct> mainProductListDB = new ArrayList<>();
        mainProductListDB.addAll(main.getProductList());

        for(PackagedProduct product:mainProductListDB){
                if (product.getContainer()==null){
                    packWithoutContainer.add(product);
                    main.getProductList().remove(product);
                }else {
                    product.setStatusOfEntity(StatusOfEntity.ARCHIVED);
                    product.setStatusOfProduct(StatusOfProduct.DELIVERED);
                    packagedProductService.save(product);
                }
            }

        if (packWithoutContainer.size()==0){
            return null;
        }

        DeliveryDocumentation newDelivery = new DeliveryDocumentation();
        newDelivery.setProductList(packWithoutContainer);
        newDelivery.setDateOfUnloading(main.getDateOfUnloading());
        newDelivery.setTimeOfUnloading(main.getTimeOfUnloading());
        newDelivery.setClientName(main.getClientName());
        newDelivery.setDescription(main.getDescription());

        newDelivery.setOrderInfo(orderInfo);
        newDelivery.setContrAgent(main.getContrAgent());
        newDelivery.setBreedOfTree(main.getBreedOfTree());
        newDelivery.setDriverInfo(main.getDriverInfo());

        deliveryDocumentationService.save(newDelivery);
        return newDelivery;
    }

// REST REST REST
    @ResponseBody
    @PostMapping("/setContainer")
    public  void setContainer(String[] arrayOfPackagesId, String containerId){
        packagedProductService.setContainer(arrayOfPackagesId,containerId);
    }

}
