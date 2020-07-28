package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.jpa.storages.TreeStorageJPA;
import com.swida.documetation.data.service.storages.TreeStorageService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreeStorageServiceImpl implements TreeStorageService {
    private TreeStorageJPA treeStorageJPA;
    private ContrAgentService contrAgentService;

    @Autowired
    public TreeStorageServiceImpl(TreeStorageJPA treeStorageJPA, ContrAgentService contrAgentService) {
        this.treeStorageJPA = treeStorageJPA;
        this.contrAgentService = contrAgentService;
    }


    @Override
    public void save(TreeStorage ts) {
        treeStorageJPA.save(ts);
    }

    @Override
    public void putNewTreeStorageObj(TreeStorage treeStorage) {
        String nameOfTreeProvider = treeStorage.getContrAgent().getNameOfAgent();
        if(contrAgentService.existByNameOfProvider(nameOfTreeProvider)==0){
            contrAgentService.save(treeStorage.getContrAgent());
        }else {
            treeStorage.getContrAgent().setId(contrAgentService.getIdByUsername(nameOfTreeProvider));
        }
        treeStorageJPA.save(treeStorage);
    }

    @Override
    public TreeStorage findById(int id) {
        return treeStorageJPA.getOne(id);
    }

    @Override
    public List<TreeStorage> findAll() {
        return treeStorageJPA.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    @Override
    public List<TreeStorage> getListByUserByBreed(int breedId, int userId) {
        return treeStorageJPA.getListByUserByBreed(breedId,userId);
    }

    @Override
    public void deleteByID(int id) {
        treeStorageJPA.deleteById(id);
    }
}
