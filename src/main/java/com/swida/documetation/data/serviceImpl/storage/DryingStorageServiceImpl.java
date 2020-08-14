package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.DryingStorage;
import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.jpa.storages.DryingStorageJPA;
import com.swida.documetation.data.service.storages.DryingStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        dryingStorageJPA.save(ds);
    }

    @Override
    public DryingStorage findById(int id) {
        return dryingStorageJPA.getOne(id);
    }

    @Override
    public DryingStorage createFromRawStorage(RawStorage rawStorage) {
        DryingStorage dryingStorage = new DryingStorage();
        dryingStorage.setCodeOfProduct(rawStorage.getCodeOfProduct());
        dryingStorage.setBreedOfTree(rawStorage.getBreedOfTree());
        dryingStorage.setBreedDescription(rawStorage.getBreedDescription());

        dryingStorage.setSizeOfHeight(rawStorage.getSizeOfHeight());
        dryingStorage.setSizeOfLong(rawStorage.getSizeOfLong());
        dryingStorage.setSizeOfWidth(rawStorage.getSizeOfWidth());

        dryingStorage.setCountOfDesk(rawStorage.getCountOfDesk());

        dryingStorage.setExtent(rawStorage.getExtent());
        dryingStorage.setDescription(rawStorage.getDescription());
        dryingStorage.setDate(rawStorage.getDate());

        dryingStorage.setUserCompany(rawStorage.getUserCompany());
        dryingStorage.setRawStorage(rawStorage);

        return dryingStorage;
    }

    @Override
    public List<DryingStorage> findAll() {
        return dryingStorageJPA.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    @Override
    public List<DryingStorage> getListByUserByBreed(int breedId, int userId) {
        if(breedId==2){
            return dryingStorageJPA.getListByUserByBreedOak(breedId,userId);
        }
        return dryingStorageJPA.getListByUserByBreed(breedId,userId);
    }

    @Override
    public void deleteByID(int id) {
        dryingStorageJPA.deleteById(id);
    }
}
