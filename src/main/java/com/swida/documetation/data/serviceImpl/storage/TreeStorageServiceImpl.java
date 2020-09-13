package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import com.swida.documetation.data.jpa.storages.TreeStorageJPA;
import com.swida.documetation.data.service.storages.TreeStorageService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        if(ts.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            ts.setBreedDescription("");
        }
        Date date = new Date(System.currentTimeMillis());
        ts.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        ts.setExtent(String.format("%.3f", Float.parseFloat(ts.getExtent())).replace(',', '.'));
        treeStorageJPA.save(ts);
    }

    @Override
    public void putNewTreeStorageObj(TreeStorage treeStorage) {
        if(treeStorage.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            treeStorage.setBreedDescription("");
        }
        treeStorage.setExtent(String.format("%.3f", Float.parseFloat(treeStorage.getExtent())).replace(',', '.'));
        Date date = new Date(System.currentTimeMillis());
        treeStorage.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
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
    public List<TreeStorage> getListByUserByBreed(int breedId, int userId, StatusOfTreeStorage status) {
        return treeStorageJPA.getListByUserByBreed(breedId,userId,status);
    }

    @Override
    public List<String> getListOfUnicBreedDescription(int breedId) {
        return treeStorageJPA.getListOfUnicBreedDescription(breedId);
    }

    @Override
    public void deleteByID(int id) {
        treeStorageJPA.deleteById(id);
    }

    @Override
    public List<String> getListOfExtent(int breedId, String[] breedDesc, int[] providers, StatusOfTreeStorage status) {
        return treeStorageJPA.getListOfExtent(breedId,breedDesc,providers,status);
    }

}
