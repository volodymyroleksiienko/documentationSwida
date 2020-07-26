package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.DryingStorage;
import com.swida.documetation.data.jpa.storages.DryingStorageJPA;
import com.swida.documetation.data.service.storages.DryingStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DryingStorageServiceImpl implements DryingStorageService {
    DryingStorageJPA dryingStorageJPA;

    @Autowired
    public DryingStorageServiceImpl(DryingStorageJPA dryingStorageJPA) {
        this.dryingStorageJPA = dryingStorageJPA;
    }

    @Override
    public void save(DryingStorage ds) {
        dryingStorageJPA.save(ds);
    }

    @Override
    public DryingStorage findById(int id) {
        return dryingStorageJPA.getOne(id);
    }

    @Override
    public List<DryingStorage> findAll() {
        return dryingStorageJPA.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    @Override
    public void deleteByID(int id) {
        dryingStorageJPA.deleteById(id);
    }
}
