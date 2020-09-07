package com.swida.documetation.data.serviceImpl.storage;


import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.DryingStorage;
import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.jpa.storages.DryStorageJPA;
import com.swida.documetation.data.service.storages.DryStorageService;
import com.swida.documetation.data.service.storages.DryingStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Service
public class DryStorageServiceImpl implements DryStorageService {
    DryStorageJPA dryStorageJPA;
    private DryingStorageService dryingStorageService;

    @Autowired
    public DryStorageServiceImpl(DryStorageJPA dryStorageJPA, DryingStorageService dryingStorageService) {
        this.dryStorageJPA = dryStorageJPA;
        this.dryingStorageService = dryingStorageService;
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
    public DryStorage createFromDryingStorage(DryingStorage dryingStorage) {
        DryStorage dryStorage = new DryStorage();
        dryStorage.setCodeOfProduct(dryingStorage.getCodeOfProduct());
        dryStorage.setBreedOfTree(dryingStorage.getBreedOfTree());
        dryStorage.setBreedDescription(dryingStorage.getBreedDescription());

        dryStorage.setSizeOfHeight(dryingStorage.getSizeOfHeight());
        dryStorage.setSizeOfLong(dryingStorage.getSizeOfLong());
        dryStorage.setSizeOfWidth(dryingStorage.getSizeOfWidth());

        dryStorage.setCountOfDesk(dryingStorage.getCountOfDesk());

        dryStorage.setExtent(dryingStorage.getExtent());
        dryStorage.setDescription(dryingStorage.getDescription());
        dryStorage.setDate(dryingStorage.getDate());

        dryStorage.setUserCompany(dryingStorage.getUserCompany());
        dryStorage.setDryingStorage(dryingStorage);
        return dryStorage;
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
    public void editDryStorage(DryStorage dryStorage) {
        DryStorage dryStorageDB = dryStorageJPA.getOne(dryStorage.getId());
        DryingStorage dryingStorage = dryStorageDB.getDryingStorage();
        if (dryStorageDB.getBreedOfTree().getId()!=2){
            int difExtentDesk = dryStorageDB.getCountOfDesk()-dryStorage.getCountOfDesk();

            dryStorageDB.setCodeOfProduct(dryStorage.getCodeOfProduct());
            dryStorageDB.setBreedDescription(dryStorage.getBreedDescription());

            dryStorageDB.setSizeOfHeight(dryStorage.getSizeOfHeight());
            dryStorageDB.setSizeOfWidth(dryStorage.getSizeOfWidth());
            dryStorageDB.setSizeOfLong(dryStorage.getSizeOfLong());
            dryStorageDB.setCountOfDesk(dryStorage.getCountOfDesk());


            dryingStorage.setCountOfDesk(dryingStorage.getCountOfDesk()+difExtentDesk);
        }else{
            float difExtent = Float.parseFloat(dryStorageDB.getExtent())-Float.parseFloat(dryingStorage.getExtent());

            dryStorageDB.setCodeOfProduct(dryStorage.getCodeOfProduct());
            dryStorageDB.setBreedDescription(dryStorage.getBreedDescription());

            dryStorageDB.setSizeOfHeight(dryStorage.getSizeOfHeight());
            dryStorageDB.setExtent(dryStorage.getExtent());
            dryStorageDB.setDescription(dryStorage.getDescription());


            dryingStorage.setExtent(
                    String.format("%.3f",Float.parseFloat(dryingStorage.getExtent())+difExtent)
                            .replace(",",".")
            );
        }
        dryingStorageService.save(dryingStorage);
        save(dryStorageDB);
    }

    @Override
    public void deleteByID(int id) {
        dryStorageJPA.deleteById(id);
    }

    @Override
    public List<String> getListOfUnicBreedDescription(int breedId) {
        return dryStorageJPA.getListOfUnicBreedDescription(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfHeight(int breedId) {
        return dryStorageJPA.getListOfUnicSizeOfHeight(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfWidth(int breedId) {
        return dryStorageJPA.getListOfUnicSizeOfWidth(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfLong(int breedId) {
        return dryStorageJPA.getListOfUnicSizeOfLong(breedId);
    }

    @Override
    public List<String> getExtent(int breedId, String[] breedDesc, String[] sizeHeight, String[] sizeWidth, String[] sizeLong, int[] agentId) {
        return dryStorageJPA.getExtent(breedId,breedDesc,sizeHeight,sizeWidth,sizeLong,agentId);
    }
}
