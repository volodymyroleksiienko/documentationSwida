package com.swida.documetation.data.service.storagesService;

import com.swida.documetation.data.entity.storages.TreeStorage;

import java.util.List;

public interface TreeStorageService {
    void save(TreeStorage ts);
    TreeStorage findById(int id);
    List<TreeStorage> findAll();
    void deleteByID(int id);
}
