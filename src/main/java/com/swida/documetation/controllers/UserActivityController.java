package com.swida.documetation.controllers;

import com.swida.documetation.data.enums.ContrAgentType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class UserActivityController {
    @GetMapping("/userActivity")
    public String getHistory(Model model){
        model.addAttribute("navTabName","activity");
        model.addAttribute("fragmentPathUserActivity","usersActivity");
//        model.addAttribute("tabName",breedId);
        model.addAttribute("fragmentPathTabConfig","statisticUsersTab");
//        model.addAttribute("contrAgentProviderList",contrAgentService.getListByType(ContrAgentType.PROVIDER));
//        model.addAttribute("userCompanyName", userCompanyService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
//        model.addAttribute("userCompanyList",userCompanyService.getListOfAllUsersROLE());
//        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
        return "adminPage";
    }
}
