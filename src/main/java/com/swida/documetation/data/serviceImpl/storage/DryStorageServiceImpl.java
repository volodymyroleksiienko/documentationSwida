package com.swida.documetation.data.serviceImpl.storage;


import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.enums.StatusOfEntity;
import com.swida.documetation.data.jpa.storages.DryStorageJPA;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.DryStorageService;
import com.swida.documetation.data.service.storages.DryingStorageService;
import com.swida.documetation.data.service.storages.TreeStorageService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class DryStorageServiceImpl implements DryStorageService {
    private final DryStorageJPA dryStorageJPA;
    private final DryingStorageService dryingStorageService;
    private final TreeStorageService treeStorageService;
    private final BreedOfTreeService breedOfTreeService;
    private final UserCompanyService userCompanyService;

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
        if(ds.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            ds.setBreedDescription("");
        }
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
        List<DryStorage> dryStorageList = new ArrayList<>();
        if (breedId==2){
            dryStorageList = dryStorageJPA.getListByUserByBreedOak(breedId,userId);
        }else{
            dryStorageList = dryStorageJPA.getListByUserByBreed(breedId,userId);
        }

        return dryStorageList.stream()
                .sorted((o1, o2) -> o2.getId()-o1.getId())
                .collect(Collectors.toList());
    }

    @Override
    public void countExtentRawStorageWithDeskDescription(DryStorage dryStorage) {
        if(dryStorage.getDeskOakList()==null || dryStorage.getDeskOakList().size()==0){
            return;
        }
        float extent = 0;
        for(DescriptionDeskOak deskOak:  dryStorage.getDeskOakList()){
            extent+= (Double.parseDouble(deskOak.getSizeOfWidth())
                    *Double.parseDouble(deskOak.getCountOfDesk())
                    *Double.parseDouble(dryStorage.getSizeOfHeight())
                    *Double.parseDouble(dryStorage.getSizeOfLong())
                    /1000000000);
        }
        if (Double.parseDouble(dryStorage.getExtent())!=extent){
            TreeStorage treeStorage = dryStorage.getDryingStorage().getRawStorage().getTreeStorage();
            treeStorage.setExtent(
                    String.format("%.3f",
                            Double.parseDouble(treeStorage.getExtent())+
                                    (Double.parseDouble(dryStorage.getExtent())-extent))
                            .replace(",",".")

            );
            treeStorageService.checkQualityInfo(treeStorage,dryStorage.getSizeOfHeight(),extent-Float.parseFloat(dryStorage.getExtent()));
            treeStorageService.save(treeStorage);
        }
        dryStorage.setExtent(
                String.format("%.3f",extent).replace(",",".")
        );
        dryStorageJPA.save(dryStorage);
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
            float difExtent = Float.parseFloat(dryStorageDB.getExtent())-Float.parseFloat(dryStorage.getExtent());

            dryStorageDB.setCodeOfProduct(dryStorage.getCodeOfProduct());
            dryStorageDB.setBreedDescription(dryStorage.getBreedDescription());

            dryStorageDB.setSizeOfHeight(dryStorage.getSizeOfHeight());
            dryStorageDB.setSizeOfLong(dryStorage.getSizeOfLong());
            dryStorageDB.setExtent(dryStorage.getExtent());
            dryStorageDB.setDescription(dryStorage.getDescription());


            dryingStorage.setExtent(
                    String.format("%.3f",Float.parseFloat(dryingStorage.getExtent())+difExtent)
                            .replace(",",".")
            );
            countExtentRawStorageWithDeskDescription(dryStorageDB);
        }
        if(dryingStorage.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            dryingStorage.setBreedDescription("");
        }
        dryingStorageService.save(dryingStorage);
        save(dryStorageDB);
    }

    @Override
    public void deleteByID(int id) {
        dryStorageJPA.deleteById(id);
    }

    @Override
    public void collectToOnePineEntityDry(DryStorage dryStorage, Integer[] arrOfEntity, int userId, int breedId) {
        int countOfDesk = 0;
        List<DryStorage> groupedList = new ArrayList<>();
        for(Integer id:arrOfEntity){
            DryStorage temp =dryStorageJPA.findById(id).orElse(null);
            if(temp!=null) {
                countOfDesk += temp.getCountOfDesk();
                groupedList.add(temp);
                temp.setStatusOfEntity(StatusOfEntity.GROUPED_BY_PROPERTIES);
                dryStorageJPA.save(temp);
            }
        }

        dryStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        dryStorage.setUserCompany(userCompanyService.findById(userId));
        dryStorage.setGroupedElements(groupedList);
        dryStorage.setCountOfDesk(countOfDesk);
        save(dryStorage);
    }

    @Override
    public void uncollectFromOnePineEntityDry(DryStorage dryStorage, int userId, int breedId) {
        if(dryStorage.getGroupedElements()!=null && dryStorage.getGroupedElements().size()>0){
            for(DryStorage grouped:dryStorage.getGroupedElements()){
                grouped.setStatusOfEntity(StatusOfEntity.ACTIVE);
                save(grouped);
            }
            deleteByID(dryStorage.getId());
        }
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
        if(breedId==2){
            return dryStorageJPA.getExtentOak(breedId,breedDesc,sizeHeight,agentId);
        }
        return dryStorageJPA.getExtent(breedId,breedDesc,sizeHeight,sizeWidth,sizeLong,agentId);
    }
}
