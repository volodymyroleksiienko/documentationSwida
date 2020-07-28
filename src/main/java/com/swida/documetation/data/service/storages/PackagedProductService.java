package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.PackagedProduct;

import java.util.List;

public interface PackagedProductService {
    void save(PackagedProduct packProd);
    void saveAll(List<PackagedProduct> productList);
    PackagedProduct findById(int id);
    List<PackagedProduct> findAll();
    void deleteByID(int id);
}
