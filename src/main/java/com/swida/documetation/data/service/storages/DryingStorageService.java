package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.DryingStorage;
import com.swida.documetation.data.entity.storages.RawStorage;

import java.util.List;

public interface DryingStorageService {
    void save(DryingStorage ds);
    DryingStorage findById(int id);
    List<DryingStorage> findAll();
    List<DryingStorage> getListByUserByBreed(int breedId, int userId);
    void deleteByID(int id);
}
