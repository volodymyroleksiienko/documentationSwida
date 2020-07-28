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
        float width = Float.parseFloat(ds.getSizeOfWidth())/1000;
        float height = Float.parseFloat(ds.getSizeOfHeight())/1000;
        float longSize = Float.parseFloat(ds.getSizeOfLong())/1000;
        int count = ds.getCountOfDesk();
        float extent = width*height*longSize*count;
        ds.setExtent(String.valueOf(extent));
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
    public List<DryingStorage> getListByUserByBreed(int breedId, int userId) {
        return dryingStorageJPA.getListByUserByBreed(breedId,userId);
    }

    @Override
    public void deleteByID(int id) {
        dryingStorageJPA.deleteById(id);
    }
}
