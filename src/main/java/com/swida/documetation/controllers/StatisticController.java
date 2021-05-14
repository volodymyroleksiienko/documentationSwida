package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.*;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.*;
import com.swida.documetation.data.service.subObjects.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class StatisticController {
    private final TreeStorageService treeStorageService;
    private final RawStorageService rawStorageService;
    private final DryingStorageService dryingStorageService;
    private final DryStorageService dryStorageService;
    private final PackagedProductService packagedProductService;
    private final DeliveryDocumentationService deliveryDocumentationService;
    private final BreedOfTreeService breedOfTreeService;
    private final ContrAgentService contrAgentService;
    private final UserCompanyService userCompanyService;
    private final OrderInfoService orderInfoService;
    private final DriverInfoService driverInfoService;
    private final QualityStatisticInfoService statisticInfoService;
    private float mainExtent = 0;

    public StatisticController(TreeStorageService treeStorageService, RawStorageService rawStorageService, DryingStorageService dryingStorageService, DryStorageService dryStorageService, PackagedProductService packagedProductService, DeliveryDocumentationService deliveryDocumentationService, BreedOfTreeService breedOfTreeService, ContrAgentService contrAgentService, UserCompanyService userCompanyService, OrderInfoService orderInfoService, DriverInfoService driverInfoService, QualityStatisticInfoService statisticInfoService) {
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
        return list.stream().map(String::trim).collect(Collectors.toSet());
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
        list.addAll(packagedProductService.getListOfUnicSizeOfWidth(breedId));
        System.out.println(packagedProductService.getListOfUnicSizeOfWidth(breedId));
        return list;
    }

    private Set<String> getAllSizeOfLong(int breedId){
        Set<String> list = new TreeSet<>();
        list.addAll(rawStorageService.getListOfUnicSizeOfLong(breedId));
        list.addAll(dryStorageService.getListOfUnicSizeOfLong(breedId));
        list.addAll(dryingStorageService.getListOfUnicSizeOfLong(breedId));
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
        json.put("deliveredExtent",
                String.format("%.3f",
                        Double.parseDouble(json.getAsString("providerInWork"))
                                + Double.parseDouble(json.getAsString("deliveryCountry"))
                                + Double.parseDouble(json.getAsString("deliveryPort"))
                                + Double.parseDouble(json.getAsString("deliveryMultimodal"))
                        ).replace(",","."));

        json.put("storageExtent",
                String.format("%.3f",
                        mainExtent
                                - Double.parseDouble(json.getAsString("providerInWork"))
                                - Double.parseDouble(json.getAsString("deliveryCountry"))
                                - Double.parseDouble(json.getAsString("deliveryPort"))
                                - Double.parseDouble(json.getAsString("deliveryMultimodal"))

                        ).replace(",","."));
        return json;
    }

    private String formatExtent(List<String> list){
        float extent = countExtent(list);
        mainExtent+=extent;
        return String.format("%.3f",extent).replace(",",".");
    }

    @GetMapping("/getDailyFactoryPowerStatistic")
    public String getDailyFactoryPowerStatistic(Model model) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserCompany user = userCompanyService.findByUsername(authentication.getName());

        if(user!=null) {
            model.addAttribute("userId", user.getId());
        }
        model.addAttribute("navTabName","main");
        model.addAttribute("fragmentPathUserStatistics","usersStatistics");
        model.addAttribute("fragmentPathTabConfig","statisticTab");
        model.addAttribute("tabName","dailyStatistic");
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        List<UserCompany> userCompanies = userCompanyService.getListOfAllUsersROLE();
        List<BreedOfTree> breedOfTreeList = breedOfTreeService.findAll();
        if(user==null || user.getRole().equals(Roles.ROLE_ADMIN)) {
            model.addAttribute("userCompanyList", userCompanies);
        }else{
            model.addAttribute("userCompanyList", Collections.singleton(user));
        }
        model.addAttribute("breedOfTreeList",breedOfTreeList);
        model.addAttribute("statisticInfo",statisticInfoService.findAll());
        Set<String> desc = new TreeSet<>();
        Set<String> heights = new TreeSet<>();
        for(BreedOfTree breedOfTree:breedOfTreeList){
            int breedId = breedOfTree.getId();
            desc.addAll(rawStorageService.getListOfUnicBreedDescription(breedId).stream().map(String::trim).collect(Collectors.toSet()));
            heights.addAll(rawStorageService.getListOfUnicSizeOfHeight(breedId));
        }
        model.addAttribute("descriptions",desc);
        model.addAttribute("thickness",heights);

        String dayFrom = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()-24*60*60*1000));
        String dayTo = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        List<QualityStatisticInfo> info = getFilteredStatisticInfo(model,breedOfTreeList.stream().map(BreedOfTree::getId).collect(Collectors.toList()), userCompanies.stream().map(UserCompany::getId).collect(Collectors.toList()),new ArrayList<>(heights),new ArrayList<>(desc),dayFrom,dayTo);
        if(user!=null && user.getRole().equals(Roles.ROLE_USER)){
            info = info.stream()
                    .filter(temp-> temp.getTreeStorage().getUserCompany().getId()==user.getId())
                    .collect(Collectors.toList());
            model.addAttribute("userCompanyList",Collections.singleton(user));
        }else {
            model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        }
        Set<UserCompany> userSet = new HashSet<>();
        for(QualityStatisticInfo st:info){
            userSet.add(st.getTreeStorage().getUserCompany());
        }
        System.out.println(userSet);
        System.out.println(info);
        model.addAttribute("filteredUsers",userSet);
        model.addAttribute("statisticInfo",info);
        return "adminPage";
    }

    @PostMapping("/getDailyFactoryPowerStatistic")
    public String getDailyFactoryPowerStatistic(Integer[] breedId, Integer[] users,String[] thickness,String[] description,String dayFrom, String dayTo,Model model) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserCompany user = userCompanyService.findByUsername(authentication.getName());
        if(user!=null) {
            model.addAttribute("userId", user.getId());
        }
        model.addAttribute("navTabName","main");
        model.addAttribute("fragmentPathUserStatistics","usersStatistics");
        model.addAttribute("fragmentPathTabConfig","statisticTab");
        model.addAttribute("tabName","dailyStatistic");
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        List<BreedOfTree> breedOfTreeList = breedOfTreeService.findAll();
        model.addAttribute("breedOfTreeList",breedOfTreeList);

        Set<String> desc = new TreeSet<>();
        Set<String> heights = new TreeSet<>();
        for(BreedOfTree breedOfTree:breedOfTreeList){
            int breedOfTreeId = breedOfTree.getId();
            desc.addAll(rawStorageService.getListOfUnicBreedDescription(breedOfTreeId).stream().map(String::trim).collect(Collectors.toSet()));
            heights.addAll(rawStorageService.getListOfUnicSizeOfHeight(breedOfTreeId));
        }
        model.addAttribute("descriptions",desc);
        model.addAttribute("thickness",heights);

        List<Integer> breedList,usersList;
        List<String> thicknessList,descriptionList;

        if(breedId!=null) {
            breedList = Arrays.asList(breedId);
        }else {
            breedList = new ArrayList<>();
        }
        if(users!=null) {
            usersList = Arrays.asList(users);
        }
        else {
            usersList = new ArrayList<>();
        }
        if(thickness!=null) {
            thicknessList = Arrays.asList(thickness);
        }else {
            thicknessList = new ArrayList<>();
        }
        if(description!=null) {
            descriptionList = Arrays.asList(description);
        }else {
            descriptionList = new ArrayList<>();
        }


        List<QualityStatisticInfo> info = getFilteredStatisticInfo(model,breedList,usersList,thicknessList,descriptionList,dayFrom,dayTo);
        if(user!=null && user.getRole().equals(Roles.ROLE_USER)){
            info = info.stream()
                    .filter(temp-> temp.getTreeStorage().getUserCompany().getId()==user.getId())
                    .collect(Collectors.toList());
            model.addAttribute("userCompanyList",Collections.singleton(user));
        }else {
            model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        }
        Set<UserCompany> userCompanies = new HashSet<>();
        for(QualityStatisticInfo st:info){
            userCompanies.add(st.getTreeStorage().getUserCompany());
        }
        model.addAttribute("filteredUsers",userCompanies);
        model.addAttribute("statisticInfo",info);
        return "adminPage";
    }

    private List<QualityStatisticInfo> getFilteredStatisticInfo(Model model,List<Integer> breedId, List<Integer> users,
                                                                List<String> thickness,List<String> desc,String dayFrom, String dayTo) throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserCompany user = userCompanyService.findByUsername(authentication.getName());
        if(breedId==null || breedId.size()==0) {
           breedId = breedOfTreeService.findAll().stream().map(BreedOfTree::getId).collect(Collectors.toList());
        }

        if(users==null || users.size()==0) {
            if (user != null && user.getRole().equals(Roles.ROLE_USER)) {
                users = new ArrayList<>();
                users.add(user.getId());
            } else {
                users = userCompanyService.getListOfAllUsersROLE().stream()
                        .map(UserCompany::getId).collect(Collectors.toList());
            }
        }

        if(thickness==null || thickness.size()==0) {
            Set<String> heights = new TreeSet<>();
            for(int breedOfTreeId:breedId){
                 heights.addAll(rawStorageService.getListOfUnicSizeOfHeight(breedOfTreeId));
            }
        }

        if(desc==null || desc.size()==0) {
            Set<String> descList = new TreeSet<>();
            for(int breedOfTreeId:breedId){
                descList.addAll(rawStorageService.getListOfUnicBreedDescription(breedOfTreeId).stream().map(String::trim).collect(Collectors.toSet()));
            }
        }

        Date dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(dayFrom);
        Date dateTo = new SimpleDateFormat("yyyy-MM-dd").parse(dayTo);
        List<QualityStatisticInfo>  infoList = statisticInfoService.findByUserByBreedByHeightByDescription(users,breedId,thickness,desc);
        List<QualityStatisticInfo> filteredInfo = new ArrayList<>();
        if(infoList!=null) {
            for (QualityStatisticInfo info : infoList) {
                if(info.getDate()!=null && !info.getDate().isEmpty()) {
                    Date current = new SimpleDateFormat("yyyy-MM-dd").parse(info.getDate());
                    if (current.compareTo(dateFrom) >= 0 && current.compareTo(dateTo) <= 0) {
                        filteredInfo.add(info);
                    }
                }
            }
        }
        model.addAttribute("checkedBreed",breedId);
        model.addAttribute("checkedUsers",users);
        model.addAttribute("checkedThickness",thickness);
        model.addAttribute("checkedDescription",desc);
        return filteredInfo;
    }

    //Statistic for users
    @GetMapping("/getUserStatistics-{breedId}-{userId}")
    public String getUserStatistics(@PathVariable("breedId")int breedId,@PathVariable("userId") int userId, Model model){
        model.addAttribute("navTabName","main");
        model.addAttribute("fragmentPathAdminStatistics","statisticsPine");
        model.addAttribute("tabName",breedId);
        model.addAttribute("fragmentPathTabConfig","statisticUsersTab");
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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean hasAdminRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN") || r.getAuthority().equals("ROLE_SUPER_USER"));
        List<ContrAgent> agents;
        if(hasAdminRole){
            agents =  contrAgentService.getListByType(ContrAgentType.PROVIDER);
        }else{
            agents = new ArrayList<>();
            agents.add(userCompanyService.findById(userId).getContrAgent());
        }
        model.addAttribute("providerList",agents);
        return "adminPage";
    }
}
