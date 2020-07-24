package com.swida.documetation.data.service.storagesService;

import com.swida.documetation.data.entity.storages.DryStorage;

import java.util.List;

public interface DryStorageService {
    void save(DryStorage ds);
    DryStorage findById(int id);
    List<DryStorage> findAll();
    void deleteByID(int id);
}
