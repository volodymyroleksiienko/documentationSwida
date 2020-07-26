package com.swida.documetation.data.serviceImpl.subObjects;

import com.swida.documetation.data.entity.subObjects.TreeProvider;
import com.swida.documetation.data.jpa.subObjects.TreeProviderJPA;
import com.swida.documetation.data.service.subObjects.TreeProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreeProviderServiceImpl implements TreeProviderService {
    TreeProviderJPA treeProviderJPA;

    @Autowired
    public TreeProviderServiceImpl(TreeProviderJPA treeProviderJPA) {
        this.treeProviderJPA = treeProviderJPA;
    }

    @Override
    public void save(TreeProvider provider) {
        treeProviderJPA.save(provider);
    }

    @Override
    public TreeProvider findById(int id) {
        return treeProviderJPA.getOne(id);
    }

    @Override
    public List<TreeProvider> findAll() {
        return treeProviderJPA.findAll();
    }

    @Override
    public int existByNameOfProvider(String name) {
        return treeProviderJPA.countExistObj(name);
    }

    @Override
    public int getIdByUsername(String name) {
        return treeProviderJPA.getIdByUsername(name);
    }


    @Override
    public void deleteByID(int id) {
        treeProviderJPA.deleteById(id);
    }
}
