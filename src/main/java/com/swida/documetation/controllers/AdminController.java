package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.ContrAgentType;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.swing.plaf.synth.ColorType;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private UserCompanyService userCompanyService;
    private BreedOfTreeService breedOfTreeService;
    private ContrAgentService contrAgentService;

    @Autowired
    public AdminController(UserCompanyService userCompanyService,BreedOfTreeService breedOfTreeService,ContrAgentService contrAgentService) {
        this.userCompanyService = userCompanyService;
        this.breedOfTreeService = breedOfTreeService;
        this.contrAgentService = contrAgentService;
    }

//    User Company Page

    @GetMapping("/getListOfUserCompany")
    public String getListOfUSerCompany(Model model){
        model.addAttribute("fragmentPathUserCompany","userCompany");
        model.addAttribute("tabName","userCompany");
        model.addAttribute("fragmentPathTabConfig","adminDashboard");
        model.addAttribute("userCompanyList",userCompanyService.findAll());
        return "adminPage";
    }

    @PostMapping("/addUser")
    public String addUser(UserCompany userCompany){

        userCompanyService.save(userCompany);
        return "redirect:/admin/getListOfUserCompany";
    }

    @PostMapping("/editUser")
    public String editUser(UserCompany userCompany){
        userCompanyService.save(userCompany);
        return "redirect:/admin/getListOfUserCompany";
    }

//    Breed of tree page
    @GetMapping("/getListBreedOfTree")
    public String getListBreedOfTree(Model model){
        model.addAttribute("fragmentPathBreedOfTree","breedOfTree");
        model.addAttribute("tabName","breedOfTree");
        model.addAttribute("fragmentPathTabConfig","adminDashboard");
        model.addAttribute("breedOfTreeList",breedOfTreeService.findAll());
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

//    Contr Agent Page
    @GetMapping("/getListOfContrAgents-{id}")
    public String getListOfContrAgents(@PathVariable("id") int id, Model model){
        if (id==2){
            model.addAttribute("fragmentPathContrAgent","contrAgentForeign");
        }else {
            model.addAttribute("fragmentPathContrAgent", "contrAgent");
        }
        model.addAttribute("tabName",""+id);
        model.addAttribute("companyType",id);
        model.addAttribute("fragmentPathTabConfig","contrAgent");
        model.addAttribute("contrAgentList",contrAgentService.getListByType(getTypeOfContrAgent(id)));
        return "adminPage";
    }

    @PostMapping("/addCompany-{id}")
    public String addCompany(@PathVariable("id") int id, ContrAgent contrAgent){
        contrAgent.setContrAgentType(getTypeOfContrAgent(id));
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
        model.addAttribute("fragmentPathOrderInfo", "contracts");
        model.addAttribute("tabName","contract");
        model.addAttribute("fragmentPathTabConfig","orders");
        model.addAttribute("orderInfoList","");
        model.addAttribute("breedOfTreeList","");
        model.addAttribute("contrAgentList","");
        return "adminPage";
    }
    @GetMapping("/getListOfOrders")
    public String getListOfOrders(Model model){
        model.addAttribute("fragmentPathOrderInfo", "orders");
        model.addAttribute("tabName","orders");
        model.addAttribute("fragmentPathTabConfig","orders");
        model.addAttribute("orderInfoList","");
        return "adminPage";
    }
}
