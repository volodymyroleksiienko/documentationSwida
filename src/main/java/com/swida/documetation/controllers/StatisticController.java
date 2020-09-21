package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.ContrAgentType;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfProduct;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.*;
import com.swida.documetation.data.service.subObjects.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;
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
    private float mainExtent = 0;

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

    @GetMapping("/getStatistics-{breedId}")
    public String getPineStatistics(@PathVariable("breedId")int breedId, Model model){
        model.addAttribute("navTabName","main");
        model.addAttribute("fragmentPathAdminStatistics","statisticsPine");
        model.addAttribute("tabName",breedId);
        model.addAttribute("fragmentPathTabConfig","statisticTab");
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());

        model.addAttribute("descList",getAllBreedDescription(breedId));
        model.addAttribute("sizeOfHeightList",getAllSizeOfHeight(breedId));
        if(breedId!=2){
            model.addAttribute("sizeOfWidthList",getAllSizeOfWidth(breedId));
        }
        model.addAttribute("sizeOfLongList",getAllSizeOfLong(breedId));
        model.addAttribute("providerList",contrAgentService.getListByType(ContrAgentType.PROVIDER));

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

//    @ResponseBody
//    @GetMapping("/getStatistic")
//    public String getStatistic(){
////        System.out.println(getAllSizeOfHeight(1));
////        System.out.println(getAllSizeOfWidth(1));
////        System.out.println(getAllSizeOfLong(1));
//        List<String> list = new ArrayList<>();
//        list.add("палетка");
//        list.add("dadsad");
//
//        return countExtent(treeStorageService.getListOfExtent(1,list))+"";
//    }

    private Set<String> getAllBreedDescription(int breedId){
        Set<String> list = new TreeSet<>();
        list.addAll(treeStorageService.getListOfUnicBreedDescription(breedId));
        list.addAll(rawStorageService.getListOfUnicBreedDescription(breedId));
        list.addAll(dryStorageService.getListOfUnicBreedDescription(breedId));
        list.addAll(dryingStorageService.getListOfUnicBreedDescription(breedId));
        System.out.println(packagedProductService.getListOfUnicBreedDescription(breedId));
        list.addAll(packagedProductService.getListOfUnicBreedDescription(breedId));
        list.addAll(orderInfoService.getListOfUnicBreedDescription(breedId));
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
        if(breedId!=2) {
            list.addAll(rawStorageService.getListOfUnicSizeOfLong(breedId));
            list.addAll(dryStorageService.getListOfUnicSizeOfLong(breedId));
            list.addAll(dryingStorageService.getListOfUnicSizeOfLong(breedId));
        }
        System.out.println(packagedProductService.getListOfUnicSizeOfLong(breedId));
        list.addAll(packagedProductService.getListOfUnicSizeOfLong(breedId));
        return list;
    }


    private float countExtent(List<String> list){
        float extent = 0;
        for(String ex:list){
            extent += Float.parseFloat(ex.replace(",","."));
        }
        return extent;
    }

    private void initializeVoidValue(int breedId,String[] descriptions, String[] sizeOfHeight,String[]sizeOfWidth,String[] sizeOfLong,
                                     int[]providers,String[] stages){
        descriptions = new String[getAllBreedDescription(breedId).size()];
                getAllBreedDescription(breedId).toArray(descriptions);
        System.out.println(descriptions);
        sizeOfHeight = getAllSizeOfHeight(breedId).toArray(descriptions);
        if(breedId!=2) {
            sizeOfWidth = getAllSizeOfWidth(breedId).toArray(descriptions);
        }
        sizeOfLong = getAllSizeOfLong(breedId).toArray(descriptions);
        List<ContrAgent> agents = contrAgentService.getListByType(ContrAgentType.PROVIDER);
        int[] newProvider = new int[agents.size()];
        for(int i=0; i<agents.size();i++){
            newProvider[i] = agents.get(i).getId();
        }
        providers = newProvider;

        String[] newStages = new String[10];
        newStages[0] = "treeStorage";
        newStages[1] = "rawStorage";
        newStages[2] = "dryingStorage";
        newStages[3] = "dryStorage";
        newStages[4] = "packagedProduct";
        newStages[5] = "deliveryMultimodal";
        newStages[6] = "deliveryPort";
        newStages[7] = "deliveryCountry";
        newStages[8] = "providerInWork";
        newStages[9] = "recycleStorage";

        stages = newStages;
    }

    @ResponseBody
    @PostMapping("/getStatisticInfo-{breedId}")
    public JSONObject getStatisticInfo(@PathVariable("breedId")int breedId, String[] descriptions, String[] sizeOfHeight,
                                   String[]sizeOfWidth,String[] sizeOfLong,int[]providers,String[] stages){
//       if(descriptions==null && sizeOfHeight==null && sizeOfWidth==null && sizeOfLong==null && providers==null &&
//               stages==null){
//           initializeVoidValue(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers,stages);
//       }

        for(int i=0;i<descriptions.length;i++){
            if(descriptions[i].equals("noDesc")){
                descriptions[i] = "";
            }
        }

        mainExtent=0;
        JSONObject json = new JSONObject();
        json.put("treeStorage","0.000");
        json.put("rawStorage","0.000");
        json.put("dryingStorage","0.000");
        json.put("dryStorage","0.000");
        json.put("packagedProduct","0.000");
        json.put("deliveryMultimodal","0.000");
        json.put("deliveryPort","0.000");
        json.put("deliveryCountry","0.000");
        json.put("providerInWork","0.000");
        json.put("recycleStorage","0.000");

        for(String stg:stages){
            switch (stg){
            case "treeStorage":
                System.out.println(treeStorageService.getListOfExtent(breedId,descriptions,providers, StatusOfTreeStorage.TREE));
                json.put("treeStorage",formatExtent(treeStorageService.getListOfExtent(breedId,descriptions,providers, StatusOfTreeStorage.TREE)));
                break;

            case "rawStorage":
                System.out.println(rawStorageService.getExtent(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers));
                json.put("rawStorage",formatExtent(rawStorageService.getExtent(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers)));
                break;

            case "dryingStorage":
                 System.out.println(dryingStorageService.getExtent(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers));
                 json.put("dryingStorage",formatExtent(dryingStorageService.getExtent(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers)));
                 break;

            case "dryStorage":
                System.out.println(dryStorageService.getExtent(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers));
                json.put("dryStorage",formatExtent(dryStorageService.getExtent(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers)));
                break;

            case "packagedProduct":
                System.out.println(packagedProductService.getExtent(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers,StatusOfProduct.ON_STORAGE));
                json.put("packagedProduct",formatExtent(packagedProductService.getExtent(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers,StatusOfProduct.ON_STORAGE)));
                break;

            case "deliveryMultimodal":
                System.out.println(packagedProductService.getExtentByOrder(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers, DeliveryDestinationType.MULTIMODAL));
                json.put("deliveryMultimodal",formatExtent(packagedProductService.getExtentByOrder(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers,DeliveryDestinationType.MULTIMODAL)));
                break;

            case "deliveryPort":
                System.out.println(packagedProductService.getExtentByOrder(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers, DeliveryDestinationType.PORT));
                json.put("deliveryPort",formatExtent(packagedProductService.getExtentByOrder(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers,DeliveryDestinationType.PORT)));
                break;

            case "deliveryCountry":
                System.out.println(packagedProductService.getExtentByOrder(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers, DeliveryDestinationType.COUNTRY));
                json.put("deliveryCountry",formatExtent(packagedProductService.getExtentByOrder(breedId,descriptions,sizeOfHeight,sizeOfWidth,sizeOfLong,providers,DeliveryDestinationType.COUNTRY)));
                break;

            case "providerInWork":
                System.out.println(orderInfoService.getExtentProviderInWork(breedId,providers));
                json.put("providerInWork",formatExtent(orderInfoService.getExtentProviderInWork(breedId,providers)));
                break;

            case "recycleStorage":
                System.out.println(treeStorageService.getListOfExtent(breedId,descriptions,providers, StatusOfTreeStorage.RECYCLING));
                json.put("recycleStorage",formatExtent(treeStorageService.getListOfExtent(breedId,descriptions,providers, StatusOfTreeStorage.RECYCLING)));
                break;

            }

        }
        json.put("mainExtent",
                String.format("%.3f",mainExtent).replace(",","."));

        return json;
    }


    private String formatExtent(List<String> list){
        float extent = countExtent(list);
        mainExtent+=extent;
        return String.format("%.3f",extent).replace(",",".");
    }
}
