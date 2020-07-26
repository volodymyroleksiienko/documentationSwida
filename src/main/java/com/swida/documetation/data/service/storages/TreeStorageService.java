package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.subObjects.TreeProvider;

import java.util.List;

public interface TreeStorageService {
    void save(TreeStorage ts);
    void putNewTreeStorageObj(TreeStorage treeStorage);
    TreeStorage findById(int id);
    List<TreeStorage> findAll();
    void deleteByID(int id);
}
