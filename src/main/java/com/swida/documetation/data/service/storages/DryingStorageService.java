package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.DryingStorage;

import java.util.List;

public interface DryingStorageService {
    void save(DryingStorage ds);
    DryingStorage findById(int id);
    List<DryingStorage> findAll();
    void deleteByID(int id);
}
