package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.BreedOfTreeDescription;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.ContrAgentType;
import com.swida.documetation.data.enums.StatusOfOrderInfo;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeDescriptionService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import com.swida.documetation.utils.other.GenerateResponseForExport;
import com.swida.documetation.utils.xlsParsers.ParseOakDeliveryInfoToXLS;
import com.swida.documetation.utils.xlsParsers.ParserDeliveryDocumentationToXLS;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.plaf.synth.ColorType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserCompanyService userCompanyService;
    private BreedOfTreeService breedOfTreeService;
    private ContrAgentService contrAgentService;
    private OrderInfoService orderInfoService;
    private BreedOfTreeDescriptionService breedOfTreeDescriptionService;

    @Autowired
    public AdminController(UserCompanyService userCompanyService,BreedOfTreeService breedOfTreeService,
                           ContrAgentService contrAgentService, OrderInfoService orderInfoService,
                           BreedOfTreeDescriptionService breedOfTreeDescriptionService) {
        this.userCompanyService = userCompanyService;
        this.breedOfTreeService = breedOfTreeService;
        this.contrAgentService = contrAgentService;
        this.orderInfoService = orderInfoService;
        this.breedOfTreeDescriptionService = breedOfTreeDescriptionService;
    }

//    Statistics Page
    @GetMapping("/getPineStatistics")
    public String getPineStatistics(Model model){
        model.addAttribute("navTabName","main");
        model.addAttribute("fragmentPathAdminStatistics","statisticsPine");
        model.addAttribute("tabName","pineStats");
        model.addAttribute("fragmentPathTabConfig","statistics");
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());

        return "adminPage";
    }

    @GetMapping("/getOakStatistics")
    public String getOakStatistics(Model model){
        model.addAttribute("navTabName","main");
        model.addAttribute("fragmentPathAdminStatistics","statisticsOak");
        model.addAttribute("tabName","oakStats");
        model.addAttribute("fragmentPathTabConfig","statistics");
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());

        return "adminPage";
    }

//    User Company Page
    @GetMapping("/getListOfUserCompany")
    public String getListOfUSerCompany(Model model){
        model.addAttribute("navTabName","adminPanel");
        model.addAttribute("fragmentPathUserCompany","userCompany");
        model.addAttribute("tabName","userCompany");
        model.addAttribute("fragmentPathTabConfig","adminDashboard");
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("allUserCompanyList",userCompanyService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return "adminPage";
    }

    @PostMapping("/addUser")
    public String addUser(UserCompany userCompany){
//        userCompany.setContrAgent(contrAgentService.getObjectByName(userCompany.getContrAgent().getNameOfAgent()));
        userCompanyService.save(userCompany);
        return "redirect:/admin/getListOfUserCompany";
    }

    @PostMapping("/editUser")
    public String editUser(UserCompany userCompany){
//        userCompany.setContrAgent(contrAgentService.getObjectByName(userCompany.getContrAgent().getNameOfAgent()));
        userCompanyService.save(userCompany);
        return "redirect:/admin/getListOfUserCompany";
    }

//    Breed of tree page
    @GetMapping("/getListBreedOfTree")
    public String getListBreedOfTree(Model model){
        model.addAttribute("navTabName","adminPanel");
        model.addAttribute("fragmentPathBreedOfTree","breedOfTree");
        model.addAttribute("tabName","breedOfTree");
        model.addAttribute("fragmentPathTabConfig","adminDashboard");
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return "adminPage";
    }

    @PostMapping("/addBreedOfTree")
    public String addBreedOfTree(BreedOfTree breedOfTree){
        breedOfTreeService.save(breedOfTree);
        return "redirect:/admin/getListBreedOfTree";
    }

    @PostMapping("/editBreedOfTree")
    public String editBreedOfTree(BreedOfTree breedOfTree){
        breedOfTreeService.save(breedOfTree);
        return "redirect:/admin/getListBreedOfTree";
    }

    //    Breed of tree DESCRIPTION page
    @GetMapping("/getListBreedOfTreeDescription")
    public String getListBreedOfTreeDescription(Model model){
        model.addAttribute("navTabName","adminPanel");
        model.addAttribute("fragmentPathBreedOfTreeDescription","breedOfTreeDesc");
        model.addAttribute("tabName","breedOfTreeDescription");
        model.addAttribute("fragmentPathTabConfig","adminDashboard");
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedOfTreeDescriptionList",breedOfTreeDescriptionService.findAll());
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return "adminPage";
    }

    @PostMapping("/addBreedOfTreeDescription")
    public String addBreedOfTreeDescription(BreedOfTreeDescription description){
        breedOfTreeDescriptionService.save(description);
        return "redirect:/admin/getListBreedOfTreeDescription";
    }

    @PostMapping("/editBreedOfTreeDescription")
    public String editBreedOfTreeDescription(BreedOfTreeDescription description){
        breedOfTreeDescriptionService.save(description);
        return "redirect:/admin/getListBreedOfTreeDescription";
    }

//    Contr Agent Page
    @GetMapping("/getListOfContrAgents-{id}")
    public String getListOfContrAgents(@PathVariable("id") int id, Model model){
        if (id==2){
            model.addAttribute("fragmentPathContrAgent","contrAgentForeign");
        }else {
            model.addAttribute("fragmentPathContrAgent", "contrAgent");
        }
        model.addAttribute("navTabName","contrAgents");
        model.addAttribute("tabName",""+id);
        model.addAttribute("companyType",id);
        model.addAttribute("fragmentPathTabConfig","contrAgent");
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("contrAgentList",contrAgentService.getListByType(getTypeOfContrAgent(id)));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return "adminPage";
    }

    @PostMapping("/addCompany-{id}")
    public String addCompany(@PathVariable("id") int id, ContrAgent contrAgent){
        contrAgent.setContrAgentType(getTypeOfContrAgent(id));
        contrAgent.setId(0);
        contrAgentService.save(contrAgent);

        return "redirect:/admin/getListOfContrAgents-"+id;
    }

    @PostMapping("/editCompany-{id}")
    public String editCompany(@PathVariable("id") int id, ContrAgent contrAgent){

        contrAgent.setContrAgentType(getTypeOfContrAgent(id));
        contrAgentService.save(contrAgent);

        return "redirect:/admin/getListOfContrAgents-"+id;
    }

    private ContrAgentType getTypeOfContrAgent(int id){
        ContrAgentType agentType;
        switch (id){
            case 1: agentType = ContrAgentType.BUYER_UA;
                break;
            case 2: agentType = ContrAgentType.BUYER_FOREIGN;
                break;
            default: agentType = ContrAgentType.PROVIDER;
        }
        return agentType;
    }

//    Order page
    @GetMapping("/getListOfContract")
    public String getListOfContract(Model model){
        List<ContrAgent> contrAgentList = new ArrayList<>();
        contrAgentList.addAll(contrAgentService.getListByType(ContrAgentType.BUYER_UA));
        contrAgentList.addAll(contrAgentService.getListByType(ContrAgentType.BUYER_FOREIGN));

        model.addAttribute("navTabName","orderInfo");
        model.addAttribute("fragmentPathOrderInfo", "contracts");
        model.addAttribute("tabName","contract");
        model.addAttribute("fragmentPathTabConfig","orders");
        model.addAttribute("orderInfoList",orderInfoService.getOrdersByStatusOfOrder(StatusOfOrderInfo.MAIN));
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("contrAgentList",contrAgentList);
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("swidaSuperAdmin")){
            UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            model.addAttribute("userId", userCompany.getId());
        }

        return "adminPage";
    }

    @PostMapping("/editContarct")
    public String editContarct(OrderInfo orderInfo){
        OrderInfo orderInfoDB = orderInfoService.findById(orderInfo.getId());
        float differenceExtent = Float.parseFloat(orderInfoDB.getExtentOfOrder())-Float.parseFloat(orderInfo.getExtentOfOrder());


        orderInfoDB.setExtentOfOrder(orderInfo.getExtentOfOrder());
        orderInfoDB.setBreedDescription(orderInfo.getBreedDescription());
        orderInfoDB.setDoneExtendOfOrder(orderInfo.getDoneExtendOfOrder());
        orderInfoDB.setCodeOfOrder(orderInfo.getCodeOfOrder());
        orderInfoDB.setContrAgent(contrAgentService.findById(orderInfo.getContrAgent().getId()));
        orderInfoDB.setStatusOfOrderInfo(StatusOfOrderInfo.MAIN);
        orderInfoDB.setBreedOfTree(breedOfTreeService.getObjectByName(orderInfo.getBreedOfTree().getBreed()));
        orderInfoDB.setExtentForDistribution(String.format("%.3f",Float.parseFloat(orderInfoDB.getExtentForDistribution())-differenceExtent).replace(",","."));
        orderInfoDB.setDate(orderInfo.getDate());
        orderInfoDB.setDestinationType(orderInfo.getDestinationType());

        orderInfoService.changeDestinationTypeOfDistributed(orderInfoDB);

        orderInfoService.save(orderInfoDB);
        return "redirect:/admin/getListOfContract";
    }

    @PostMapping("/addContract")
    public String addContract(OrderInfo orderInfo){
        orderInfo.setBreedOfTree(breedOfTreeService.getObjectByName(orderInfo.getBreedOfTree().getBreed()));
        orderInfo.setExtentForDistribution(orderInfo.getExtentOfOrder());
        orderInfo.setStatusOfOrderInfo(StatusOfOrderInfo.MAIN);
        orderInfo.setDoneExtendOfOrder("0.000");
        orderInfoService.save(orderInfo);
        return "redirect:/admin/getListOfContract";
    }

    @GetMapping("/getListOfOrders")
    public String getListOfOrders(Model model){
        model.addAttribute("navTabName","orderInfo");
        model.addAttribute("fragmentPathOrderInfo", "orders");
        model.addAttribute("tabName","orders");
        model.addAttribute("fragmentPathTabConfig","orders");
        model.addAttribute("orderInfoList",orderInfoService.getOrdersByStatusOfOrder(StatusOfOrderInfo.DISTRIBUTION));
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        if (!SecurityContextHolder.getContext().getAuthentication().getName().equals("swidaSuperAdmin")){
            UserCompany userCompany = userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            model.addAttribute("userId", userCompany.getId());
        }
        return "adminPage";
    }

    @PostMapping("/editOrder")
    public String editOrder(OrderInfo orderInfo){
        OrderInfo orderInfoDB = orderInfoService.findById(orderInfo.getId());
        float differenceExtent = Float.parseFloat(orderInfoDB.getExtentOfOrder())-Float.parseFloat(orderInfo.getExtentOfOrder());
        float differenceDoneExtent = Float.parseFloat(orderInfoDB.getDoneExtendOfOrder())-Float.parseFloat(orderInfo.getDoneExtendOfOrder());



        orderInfoDB.setExtentOfOrder(orderInfo.getExtentOfOrder());
        orderInfoDB.setDoneExtendOfOrder(orderInfo.getDoneExtendOfOrder());
        orderInfoDB.setCodeOfOrder(orderInfo.getCodeOfOrder());
        orderInfoDB.setContrAgent(contrAgentService.findById(orderInfo.getContrAgent().getId()));
        orderInfoDB.setStatusOfOrderInfo(StatusOfOrderInfo.DISTRIBUTION);
        orderInfoDB.setBreedOfTree(breedOfTreeService.getObjectByName(orderInfo.getBreedOfTree().getBreed()));
        orderInfoDB.setBreedDescription(orderInfo.getBreedDescription());
        orderInfoDB.setDate(orderInfo.getDate());

        OrderInfo main = orderInfoDB.getMainOrder();
        main.setExtentForDistribution(String.format("%.3f",Float.parseFloat(main.getExtentForDistribution())+differenceExtent).replace(",","."));
        main.setDoneExtendOfOrder(String.format("%.3f",Float.parseFloat(main.getDoneExtendOfOrder())-differenceDoneExtent).replace(",","."));

        orderInfoService.save(main);
        orderInfoService.save(orderInfoDB);
        return "redirect:/admin/getListOfOrders";
    }

    @GetMapping("/getInformation")
    public String getInformation(Model model){
        model.addAttribute("navTabName","adminPanel");
        model.addAttribute("fragmentPathUserCompany","userCompany");
        model.addAttribute("tabName","userCompany");
        model.addAttribute("fragmentPathTabConfig","adminDashboard");
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());

        return "informationPage";
    }

    @PostMapping("/downloadExamplePineXls")
    public ResponseEntity<Resource> downloadExamplePineXls() throws FileNotFoundException {
        String filePath = System.getProperty("user.dir")+ File.separator +"src"+File.separator
                +"main"+File.separator+"resources"+File.separator+"static"+File.separator
                +"exampleXLS"+File.separator+"sosnaImport.xlsx";
        return new GenerateResponseForExport().generate(filePath,"","");
    }

    @PostMapping("/downloadExampleOakXls")
    public ResponseEntity<Resource> downloadExampleOakXls() throws FileNotFoundException {
        String filePath = System.getProperty("user.dir")+ File.separator +"src"+File.separator
                +"main"+File.separator+"resources"+File.separator+"static"+File.separator
                +"exampleXLS"+File.separator+"oakImport.xlsx";
        return new GenerateResponseForExport().generate(filePath,"","");
    }

}
