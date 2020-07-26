package com.swida.documetation.data.service.subObjects;

import com.swida.documetation.data.entity.subObjects.TreeProvider;

import java.util.List;

public interface TreeProviderService {
    void save(TreeProvider provider);
    TreeProvider findById(int id);
    List<TreeProvider> findAll();

    //return 0 if doesnot Exist
    int existByNameOfProvider(String name);
    int getIdByUsername(String name);
    void deleteByID(int id);
}
