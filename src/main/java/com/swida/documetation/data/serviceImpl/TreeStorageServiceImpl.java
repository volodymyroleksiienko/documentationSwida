package com.swida.documetation.data.serviceImpl;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.jpa.storagesJPA.TreeStorageJPA;
import com.swida.documetation.data.service.storagesService.TreeStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreeStorageServiceImpl implements TreeStorageService {
    TreeStorageJPA treeStorageJPA;

    @Autowired
    public TreeStorageServiceImpl(TreeStorageJPA treeStorageJPA) {
        this.treeStorageJPA = treeStorageJPA;
    }

    @Override
    public void save(TreeStorage ts) {
        treeStorageJPA.save(ts);
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
