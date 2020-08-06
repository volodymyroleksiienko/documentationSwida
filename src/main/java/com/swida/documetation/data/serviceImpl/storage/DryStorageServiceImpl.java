package com.swida.documetation.data.serviceImpl.storage;


import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.jpa.storages.DryStorageJPA;
import com.swida.documetation.data.service.storages.DryStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        if (ds.getSizeOfWidth()!=null) {
            float width = Float.parseFloat(ds.getSizeOfWidth()) / 1000;
            float height = Float.parseFloat(ds.getSizeOfHeight()) / 1000;
            float longSize = Float.parseFloat(ds.getSizeOfLong()) / 1000;
            int count = ds.getCountOfDesk();
            float extent = width * height * longSize * count;
            ds.setExtent(String.format("%.3f",extent).replace(',','.'));
        }
        Date date = new Date(System.currentTimeMillis());
        ds.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
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
    public List<DryStorage> getListByUserByBreed(int breedId, int userId) {
        if (breedId==2){
            return  dryStorageJPA.getListByUserByBreedOak(breedId,userId);
        }
        return dryStorageJPA.getListByUserByBreed(breedId,userId);
    }

    @Override
    public void deleteByID(int id) {
        dryStorageJPA.deleteById(id);
    }
}
