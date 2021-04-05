package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.jpa.storages.DryingStorageJPA;
import com.swida.documetation.data.service.storages.DryingStorageService;
import com.swida.documetation.data.service.storages.RawStorageService;
import com.swida.documetation.data.service.storages.TreeStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class DryingStorageServiceImpl implements DryingStorageService {
    private DryingStorageJPA dryingStorageJPA;
    private RawStorageService rawStorageService;
    private TreeStorageService treeStorageService;


    public DryingStorageServiceImpl(DryingStorageJPA dryingStorageJPA, RawStorageService rawStorageService, TreeStorageService treeStorageService) {
        this.dryingStorageJPA = dryingStorageJPA;
        this.rawStorageService = rawStorageService;
        this.treeStorageService = treeStorageService;
    }

    @Override
    public void save(DryingStorage ds) {
        if (ds.getSizeOfWidth()!=null && Float.parseFloat(ds.getSizeOfWidth())!=0) {
            float width = Float.parseFloat(ds.getSizeOfWidth()) / 1000;
            float height = Float.parseFloat(ds.getSizeOfHeight()) / 1000;
            float longSize = Float.parseFloat(ds.getSizeOfLong()) / 1000;
            int count = ds.getCountOfDesk();
            float extent = width * height * longSize * count;
            ds.setExtent(String.format("%.3f",extent).replace(',','.'));
        }
        Date date = new Date(System.currentTimeMillis());
        ds.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        if(ds.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            ds.setBreedDescription("");
        }
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
    public Set<Integer> getListCellByUserByBreed(int breedId, int userId) {
        return dryingStorageJPA.getListCellByUserByBreed(breedId,userId);
    }

    @Override
    public void countExtentRawStorageWithDeskDescription(DryingStorage dryingStorage) {
        if(dryingStorage.getDeskOakList()==null || dryingStorage.getDeskOakList().size()==0){
            return;
        }
        float extent = 0;
        for(DescriptionDeskOak deskOak:  dryingStorage.getDeskOakList()){
            extent+= (Double.parseDouble(deskOak.getSizeOfWidth())
                    *Double.parseDouble(deskOak.getCountOfDesk())
                    *Double.parseDouble(dryingStorage.getSizeOfHeight())
                    *Double.parseDouble(dryingStorage.getSizeOfLong())
                    /1000000000);
        }
        if (Double.parseDouble(dryingStorage.getExtent())!=extent){
            TreeStorage treeStorage = dryingStorage.getRawStorage().getTreeStorage();
            treeStorage.setExtent(
                    String.format("%.3f",
                            Double.parseDouble(treeStorage.getExtent())+
                                    (Double.parseDouble(dryingStorage.getExtent())-extent))
                            .replace(",",".")

            );
//          @todo
//            treeStorageService.checkQualityInfo(treeStorage,dryingStorage.getSizeOfHeight(),extent-Float.parseFloat(dryingStorage.getExtent()));
            treeStorageService.save(treeStorage);
        }
        dryingStorage.setExtent(
                String.format("%.3f",extent).replace(",",".")
        );
        dryingStorageJPA.save(dryingStorage);
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
            dryingStorageDB.setCell(dryingStorage.getCell());
            dryingStorageDB.setStartDate(dryingStorage.getStartDate());


            RawStorage rawStorage = dryingStorageDB.getRawStorage();
            rawStorage.setCountOfDesk(rawStorage.getCountOfDesk()+difExtentDesk);
            rawStorageService.save(rawStorage);
        }else{
            float difExtent = Float.parseFloat(dryingStorageDB.getExtent())-Float.parseFloat(dryingStorage.getExtent());

            dryingStorageDB.setCodeOfProduct(dryingStorage.getCodeOfProduct());
            dryingStorageDB.setBreedDescription(dryingStorage.getBreedDescription());

            dryingStorageDB.setSizeOfHeight(dryingStorage.getSizeOfHeight());
            dryingStorageDB.setSizeOfLong(dryingStorage.getSizeOfLong());
            dryingStorageDB.setExtent(dryingStorage.getExtent());
            dryingStorageDB.setDescription(dryingStorage.getDescription());
            dryingStorageDB.setDateDrying(dryingStorage.getDateDrying());
            dryingStorageDB.setCell(dryingStorage.getCell());
            dryingStorageDB.setStartDate(dryingStorage.getStartDate());

            RawStorage rawStorage = dryingStorageDB.getRawStorage();
            rawStorage.setExtent(
                    String.format("%.3f",Float.parseFloat(rawStorage.getExtent())+difExtent)
                    .replace(",",".")
                    );
            rawStorageService.save(rawStorage);
            countExtentRawStorageWithDeskDescription(dryingStorageDB);
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
        if(breedId==2){
//            for(DryingStorage dryingStorage: dryingStorageJPA.getOak(breedId,breedDesc,sizeHeight,agentId)){
//                System.out.println(dryingStorage);
//            }
            return dryingStorageJPA.getExtentOak(breedId,breedDesc,sizeHeight,agentId);
        }
        return dryingStorageJPA.getExtent(breedId,breedDesc,sizeHeight,sizeWidth,sizeLong,agentId);
    }
}
