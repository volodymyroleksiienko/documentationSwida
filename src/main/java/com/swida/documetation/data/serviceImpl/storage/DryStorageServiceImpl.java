package com.swida.documetation.data.serviceImpl.storage;


import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.jpa.storages.DryStorageJPA;
import com.swida.documetation.data.service.storages.DryStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DryStorageServiceImpl implements DryStorageService {
    DryStorageJPA dryStorageJPA;

    @Autowired
    public DryStorageServiceImpl(DryStorageJPA dryStorageJPA) {
        this.dryStorageJPA = dryStorageJPA;
    }

    @Override
    public void save(DryStorage ds) {
        dryStorageJPA.save(ds);
    }

    @Override
    public DryStorage findById(int id) {
        return dryStorageJPA.getOne(id);
    }

    @Override
    public List<DryStorage> findAll() {
        return dryStorageJPA.findAll();
    }

    @Override
    public void deleteByID(int id) {
        dryStorageJPA.deleteById(id);
    }
}
