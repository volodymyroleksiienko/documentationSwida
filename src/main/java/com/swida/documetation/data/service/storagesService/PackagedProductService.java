package com.swida.documetation.data.service.storagesService;

import com.swida.documetation.data.entity.storages.PackagedProduct;

import java.util.List;

public interface PackagedProductService {
    void save(PackagedProduct packProd);
    PackagedProduct findById(int id);
    List<PackagedProduct> findAll();
    void deleteByID(int id);
}
