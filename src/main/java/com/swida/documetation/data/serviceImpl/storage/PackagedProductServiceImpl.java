package com.swida.documetation.data.serviceImpl.storage;


import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.jpa.storages.PackagedProductJPA;
import com.swida.documetation.data.service.storages.PackagedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PackagedProductServiceImpl implements PackagedProductService {
    PackagedProductJPA productJPA;

    @Autowired
    public PackagedProductServiceImpl(PackagedProductJPA productJPA) {
        this.productJPA = productJPA;
    }

    @Override
    public void save(PackagedProduct packProd) {
        productJPA.save(packProd);
    }

    @Override
    public PackagedProduct findById(int id) {
        return productJPA.getOne(id);
    }

    @Override
    public List<PackagedProduct> findAll() {
        return productJPA.findAll();
    }

    @Override
    public void deleteByID(int id) {
        productJPA.deleteById(id);
    }
}
