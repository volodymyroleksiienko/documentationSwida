package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.storages.WasteStorage;

import java.util.List;

public interface WasteStorageService {
    void save(WasteStorage ws);
    void createWaste(TreeStorage treeStorage,String usedExtent, String deskExtent);
    WasteStorage findById(int id);
    List<WasteStorage> findAll();
    List<WasteStorage> getListByUserByBreed(int breedId, int userId);
    void deleteByID(int id);
}
