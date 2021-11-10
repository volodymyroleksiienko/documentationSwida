package com.swida.documetation.controllers;

import com.swida.documetation.data.enums.ContrAgentType;
import com.swida.documetation.data.service.LoggerDataInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class UserActivityController {
    @Autowired
    private LoggerDataInfoService loggerDataInfoService;

    @GetMapping("/userActivity")
    public String getHistory(Model model, String dateFrom, String dateTo, Integer[] breedId, Integer[] users){
        model.addAttribute("navTabName","activity");
        model.addAttribute("fragmentPathUserActivity","usersActivity");
//        model.addAttribute("tabName",breedId);
        model.addAttribute("fragmentPathTabConfig","statisticUsersTab");
//        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
//        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
//        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
//        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        model.addAttribute("activityList",loggerDataInfoService.findAll());
        return "adminPage";
    }
}
