package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.WasteStorage;

import java.util.List;

public interface WasteStorageService {
    void save(WasteStorage ws);
    WasteStorage findById(int id);
    List<WasteStorage> findAll();
    void deleteByID(int id);
}
