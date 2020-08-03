package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.storages.WasteStorage;
import com.swida.documetation.data.jpa.storages.WasteStorageJPA;
import com.swida.documetation.data.service.storages.WasteStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class WasteStorageServiceImpl implements WasteStorageService {
    WasteStorageJPA wasteStorageJPA;

    @Autowired
    public WasteStorageServiceImpl(WasteStorageJPA wasteStorageJPA) {
        this.wasteStorageJPA = wasteStorageJPA;
    }

    @Override
    public void save(WasteStorage ws) {
        wasteStorageJPA.save(ws);
    }

    @Override
    public void createWaste(TreeStorage treeStorage,String usedExtent, String deskExtent) {
        WasteStorage  wasteStorage = new WasteStorage();
        wasteStorage.setUserCompany(treeStorage.getUserCompany());
        wasteStorage.setBreedOfTree(treeStorage.getBreedOfTree());
        wasteStorage.setBreedDescription(treeStorage.getBreedDescription());
        wasteStorage.setCodeOfProduct(treeStorage.getCodeOfProduct()+"-waste");
        wasteStorage.setContrAgent(treeStorage.getContrAgent());
        wasteStorage.setDescription(treeStorage.getBreedDescription());

        wasteStorage.setExtent(String.valueOf(Float.parseFloat(usedExtent)-Float.parseFloat(deskExtent)));

        Date date = new Date(System.currentTimeMillis());
        wasteStorage.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        wasteStorageJPA.save(wasteStorage);
    }

    @Override
    public WasteStorage findById(int id) {
        return wasteStorageJPA.getOne(id);
    }

    @Override
    public List<WasteStorage> findAll() {
        return wasteStorageJPA.findAll();
    }

    @Override
    public List<WasteStorage> getListByUserByBreed(int breedId, int userId) {
        return wasteStorageJPA.getListByUserByBreed(breedId,userId);
    }

    @Override
    public void deleteByID(int id) {
        wasteStorageJPA.deleteById(id);
    }
}
