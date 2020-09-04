package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.enums.ContrAgentType;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.*;
import com.swida.documetation.data.service.subObjects.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Controller
@RequestMapping("/admin")
public class StatisticController {
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
    public StatisticController(TreeStorageService treeStorageService, RawStorageService rawStorageService, DryingStorageService dryingStorageService, DryStorageService dryStorageService, PackagedProductService packagedProductService, DeliveryDocumentationService deliveryDocumentationService, BreedOfTreeService breedOfTreeService, ContrAgentService contrAgentService, UserCompanyService userCompanyService, OrderInfoService orderInfoService, DriverInfoService driverInfoService) {
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

    @ResponseBody
    @GetMapping("/getStatistic")
    public String getStatistic(){
//        System.out.println(getAllSizeOfHeight(1));
//        System.out.println(getAllSizeOfWidth(1));
//        System.out.println(getAllSizeOfLong(1));
        List<String> list = new ArrayList<>();
        list.add("палетка");
        list.add("dadsad");

        return countExtent(treeStorageService.getListOfExtent(1,list))+"";
    }

    private Set<String> getAllBreedDescription(int breedId){
        Set<String> list = new TreeSet<>();
        list.addAll(treeStorageService.getListOfUnicBreedDescription(breedId));
        list.addAll(rawStorageService.getListOfUnicBreedDescription(breedId));
        list.addAll(dryStorageService.getListOfUnicBreedDescription(breedId));
        list.addAll(dryingStorageService.getListOfUnicBreedDescription(breedId));
        System.out.println(packagedProductService.getListOfUnicBreedDescription(breedId));
        list.addAll(packagedProductService.getListOfUnicBreedDescription(breedId));
        return list;
    }

    private Set<String> getAllSizeOfHeight(int breedId){
        Set<String> list = new TreeSet<>();
        list.addAll(rawStorageService.getListOfUnicSizeOfHeight(breedId));
        list.addAll(dryStorageService.getListOfUnicSizeOfHeight(breedId));
        list.addAll(dryingStorageService.getListOfUnicSizeOfHeight(breedId));
        System.out.println(packagedProductService.getListOfUnicSizeOfHeight(breedId));
        list.addAll(packagedProductService.getListOfUnicSizeOfHeight(breedId));
        return list;
    }

    private Set<String> getAllSizeOfWidth(int breedId){
        Set<String> list = new TreeSet<>();
        list.addAll(rawStorageService.getListOfUnicSizeOfWidth(breedId));
        list.addAll(dryStorageService.getListOfUnicSizeOfWidth(breedId));
        list.addAll(dryingStorageService.getListOfUnicSizeOfWidth(breedId));
        System.out.println(packagedProductService.getListOfUnicSizeOfWidth(breedId));
        list.addAll(packagedProductService.getListOfUnicSizeOfWidth(breedId));
        return list;
    }

    private Set<String> getAllSizeOfLong(int breedId){
        Set<String> list = new TreeSet<>();
        list.addAll(rawStorageService.getListOfUnicSizeOfLong(breedId));
        list.addAll(dryStorageService.getListOfUnicSizeOfLong(breedId));
        list.addAll(dryingStorageService.getListOfUnicSizeOfLong(breedId));
        System.out.println(packagedProductService.getListOfUnicSizeOfLong(breedId));
        list.addAll(packagedProductService.getListOfUnicSizeOfLong(breedId));
        return list;
    }


    private float countExtent(List<String> list){
        float extent = 0;
        for(String ex:list){
            extent += Float.parseFloat(ex);
        }
        return extent;
    }

}
