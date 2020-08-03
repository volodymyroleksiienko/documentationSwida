package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.enums.Roles;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.TreeStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SecurityController {
    @Autowired
    UserCompanyService userCompanyService;
    @Autowired
    TreeStorageService treeStorageService;


    @GetMapping("/")
    public String index(){
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(){
        return "redirect:/enterRequest";
    }

    @PostMapping("/enterRequest")
    public String loginPost(){
//        UserCompany company  = new UserCompany();
//        company.setRole(Roles.ROLES_USER);
//        company.setUsername("8");
//        company.setPassword("8");
//        company.setNameOfCompany("SuperPylka");
//        userCompanyService.save(company);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int userID = userCompanyService.findByUsername(auth.getName()).getId();
        return "redirect:/fabric/index-"+userID;
    }

}
