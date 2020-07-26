package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.subObjects.TreeProvider;
import com.swida.documetation.data.jpa.storages.TreeStorageJPA;
import com.swida.documetation.data.service.storages.TreeStorageService;
import com.swida.documetation.data.service.subObjects.TreeProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreeStorageServiceImpl implements TreeStorageService {
    private TreeStorageJPA treeStorageJPA;
    private TreeProviderService treeProviderService;

    @Autowired
    public TreeStorageServiceImpl(TreeStorageJPA treeStorageJPA, TreeProviderService treeProviderService) {
        this.treeStorageJPA = treeStorageJPA;
        this.treeProviderService = treeProviderService;
    }


    @Override
    public void save(TreeStorage ts) {
        treeStorageJPA.save(ts);
    }

    @Override
    public void putNewTreeStorageObj(TreeStorage treeStorage) {
        String nameOfTreeProvider = treeStorage.getTreeProvider().getNameOfTreeProvider();
        if(treeProviderService.existByNameOfProvider(nameOfTreeProvider)==0){
            treeProviderService.save(treeStorage.getTreeProvider());
        }else {
            treeStorage.getTreeProvider().setId(treeProviderService.getIdByUsername(nameOfTreeProvider));
        }
        treeStorageJPA.save(treeStorage);

    }

    @Override
    public TreeStorage findById(int id) {
        return treeStorageJPA.getOne(id);
    }

    @Override
    public List<TreeStorage> findAll() {
        return treeStorageJPA.findAll();
    }

    @Override
    public void deleteByID(int id) {
        treeStorageJPA.deleteById(id);
    }
}
