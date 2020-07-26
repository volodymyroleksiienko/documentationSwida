package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.RawStorage;

import java.util.List;

public interface RawStorageService {
    void save(RawStorage rs);
    RawStorage findById(int id);
    List<RawStorage> findAll();
    void deleteByID(int id);
}
