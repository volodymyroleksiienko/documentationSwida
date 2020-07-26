package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.subObjects.TreeProvider;
import com.swida.documetation.data.service.storages.TreeStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FabricRestController {
    private TreeStorageService treeStorageService;

    @Autowired
    public FabricRestController(TreeStorageService treeStorageService) {
        this.treeStorageService = treeStorageService;
    }

    @PutMapping("/addTreeStorageObj")
    public void addTreeStorageObj(@RequestBody TreeStorage treeStorage){
//        treeStorageService.save(treeStorage);
        treeStorageService.putNewTreeStorageObj(treeStorage);
    }

    @PostMapping("/add")
    public String add(@RequestParam List<TreeStorage> treeStorage){
        return treeStorage.toString();
    }

}
