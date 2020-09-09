package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.DryingStorage;
import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.jpa.storages.DryingStorageJPA;
import com.swida.documetation.data.service.storages.DryingStorageService;
import com.swida.documetation.data.service.storages.RawStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DryingStorageServiceImpl implements DryingStorageService {
    DryingStorageJPA dryingStorageJPA;
    private RawStorageService rawStorageService;

    @Autowired
    public DryingStorageServiceImpl(DryingStorageJPA dryingStorageJPA, RawStorageService rawStorageService) {
        this.dryingStorageJPA = dryingStorageJPA;
        this.rawStorageService = rawStorageService;
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
    public void editDryingStorage(DryingStorage dryingStorage) {
        DryingStorage dryingStorageDB = dryingStorageJPA.getOne(dryingStorage.getId());
        if (dryingStorageDB.getBreedOfTree().getId()!=2){
            int difExtentDesk = dryingStorageDB.getCountOfDesk()-dryingStorage.getCountOfDesk();

            dryingStorageDB.setCodeOfProduct(dryingStorage.getCodeOfProduct());
            dryingStorageDB.setBreedDescription(dryingStorage.getBreedDescription());

            dryingStorageDB.setSizeOfHeight(dryingStorage.getSizeOfHeight());
            dryingStorageDB.setSizeOfWidth(dryingStorage.getSizeOfWidth());
            dryingStorageDB.setSizeOfLong(dryingStorage.getSizeOfLong());
            dryingStorageDB.setCountOfDesk(dryingStorage.getCountOfDesk());
            dryingStorageDB.setDateDrying(dryingStorage.getDateDrying());

            RawStorage rawStorage = dryingStorageDB.getRawStorage();
            rawStorage.setCountOfDesk(rawStorage.getCountOfDesk()+difExtentDesk);
            rawStorageService.save(rawStorage);
        }else{
            float difExtent = Float.parseFloat(dryingStorageDB.getExtent())-Float.parseFloat(dryingStorage.getExtent());

            dryingStorageDB.setCodeOfProduct(dryingStorage.getCodeOfProduct());
            dryingStorageDB.setBreedDescription(dryingStorage.getBreedDescription());

            dryingStorageDB.setSizeOfHeight(dryingStorage.getSizeOfHeight());
            dryingStorageDB.setExtent(dryingStorage.getExtent());
            dryingStorageDB.setDescription(dryingStorage.getDescription());
            dryingStorageDB.setDateDrying(dryingStorage.getDateDrying());

            RawStorage rawStorage = dryingStorageDB.getRawStorage();
            rawStorage.setExtent(
                    String.format("%.3f",Float.parseFloat(rawStorage.getExtent())+difExtent)
                    .replace(",",".")
                    );
            rawStorageService.save(rawStorage);

        }
        save(dryingStorageDB);
    }

    @Override
    public void deleteByID(int id) {
        dryingStorageJPA.deleteById(id);
    }

    @Override
    public List<String> getListOfUnicBreedDescription(int breedId) {
        return dryingStorageJPA.getListOfUnicBreedDescription(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfHeight(int breedId) {
        return dryingStorageJPA.getListOfUnicSizeOfHeight(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfWidth(int breedId) {
        return dryingStorageJPA.getListOfUnicSizeOfWidth(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfLong(int breedId) {
        return dryingStorageJPA.getListOfUnicSizeOfLong(breedId);
    }
    @Override
    public List<String> getExtent(int breedId, String[] breedDesc, String[] sizeHeight, String[] sizeWidth, String[] sizeLong, int[] agentId) {
        return dryingStorageJPA.getExtent(breedId,breedDesc,sizeHeight,sizeWidth,sizeLong,agentId);
    }
}
