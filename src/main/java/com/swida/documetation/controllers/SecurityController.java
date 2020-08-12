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
        int userID;
        System.out.println(auth.getAuthorities());

        boolean hasUserRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_USER"));
        boolean hasSuperUserRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_SUPER_USER"));
        boolean hasAdminRole = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        if(hasUserRole || hasSuperUserRole){
            userID = userCompanyService.findByUsername(auth.getName()).getId();
            return "redirect:/fabric/index-"+userID;

        }
        if (hasAdminRole){
            return "redirect:/admin/getListOfUserCompany";
        }
        return "redirect:/logout";
    }

}
