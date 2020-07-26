package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.storages.DryingStorage;
import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.service.storages.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FabricController {
    TreeStorageService treeStorageService;
    RawStorageService rawStorageService;
    DryingStorageService dryingStorageService;
    DryStorageService dryStorageService;
    PackagedProductService packagedProductService;

    @Autowired
    public FabricController(TreeStorageService treeStorageService, RawStorageService rawStorageService, DryingStorageService dryingStorageService, DryStorageService dryStorageService, PackagedProductService packagedProductService) {
        this.treeStorageService = treeStorageService;
        this.rawStorageService = rawStorageService;
        this.dryingStorageService = dryingStorageService;
        this.dryStorageService = dryStorageService;
        this.packagedProductService = packagedProductService;
    }

    @GetMapping("/getListOfTreeStorage")
    public String getListOfTreeStorage(Model model){
        model.addAttribute("treeStorageList",treeStorageService.findAll());
        return "";
    }

    @GetMapping("/getListOfRawStorageService")
    public String getListOfRawStorageService(Model model){
        model.addAttribute("treeStorageList",rawStorageService.findAll());
        return "";
    }

    @GetMapping("/getListOfDryingStorageService")
    public String getListOfDryingStorageService(Model model){
        model.addAttribute("treeStorageList",dryingStorageService.findAll());
        return "";
    }

    @GetMapping("/getListOfDryStorageService")
    public String getListOfDryStorageService(Model model){
        model.addAttribute("treeStorageList",dryStorageService.findAll());
        return "";
    }

    @GetMapping("/getListOfPackagedProductService")
    public String getListOfPackagedProductService(Model model){
        model.addAttribute("treeStorageList",packagedProductService.findAll());
        return "";
    }
}
