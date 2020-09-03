package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.service.storages.TreeStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/admin")
public class StatisticController {
    private TreeStorageService treeStorageService;

    @Autowired
    public StatisticController(TreeStorageService treeStorageService) {
        this.treeStorageService = treeStorageService;
    }

    @ResponseBody
    @GetMapping("/getStatistic")
    public String getStatistic(){
        return treeStorageService.getListOfUnicBreedDescription().toString();
    }
}
