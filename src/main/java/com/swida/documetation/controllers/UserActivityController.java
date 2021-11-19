package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.ContrAgentType;
import com.swida.documetation.data.service.LoggerDataInfoService;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Controller
@RequestMapping("/admin")
public class UserActivityController {
    @Autowired
    private LoggerDataInfoService loggerDataInfoService;
    @Autowired
    private BreedOfTreeService breedOfTreeService;
    @Autowired
    private UserCompanyService userCompanyService;
    @Autowired
    private ContrAgentService contrAgentService;

    @GetMapping("/userActivity")
    public String getHistory(Model model, String dateFrom, String dateTo, Integer[] breedId, Integer[] users, String[] storageType, String[] actions){
        model.addAttribute("navTabName","activity");
        model.addAttribute("fragmentPathUserActivity","usersActivity");
//        model.addAttribute("tabName",breedId);
        model.addAttribute("fragmentPathTabConfig","statisticUsersTab");
        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        if((dateFrom==null || dateFrom.isEmpty()) && (dateTo==null || dateTo.isEmpty())) {
            dateFrom = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
            dateTo = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        try {
            model.addAttribute("dateTo", new SimpleDateFormat("yyyy-MM-dd").parse(dateTo));
            model.addAttribute("dateFrom", new SimpleDateFormat("yyyy-MM-dd").parse(dateFrom));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        model.addAttribute("selectedBreedId",breedId!=null?Arrays.asList(breedId):null);
        model.addAttribute("selectedUsers",users!=null?Arrays.asList(users):null);
        model.addAttribute("selectedStorage",storageType!=null?Arrays.asList(storageType):null);
        System.out.println(storageType!=null?Arrays.asList(storageType):null);
        model.addAttribute("selectedActions",actions!=null?Arrays.asList(actions):null);
        model.addAttribute("activityList",
                loggerDataInfoService.findFiltered(dateFrom,dateTo,breedId,users,storageType,actions));
        return "adminPage";
    }
}
