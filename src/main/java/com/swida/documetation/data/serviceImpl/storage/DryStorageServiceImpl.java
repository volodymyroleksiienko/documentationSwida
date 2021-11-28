package com.swida.documetation.data.serviceImpl.storage;


import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.*;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
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
    public DryStorage save(DryStorage ds) {
        if (ds.getSizeOfWidth()!=null && Float.parseFloat(ds.getSizeOfWidth())>0) {
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
        return dryStorageJPA.save(ds);
    }

    @Override
    public DryStorage findById(int id) {
        return dryStorageJPA.getOne(id);
    }

    @Override
    public List<DryStorage> findById(Integer[] idOfRows) {
        if(idOfRows!=null && idOfRows.length>0) {
            return dryStorageJPA.findByIdIn(Arrays.asList(idOfRows));
        }else {
            return new ArrayList<>();
        }
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
    public DryStorage addDryStorageWithoutParent(int userId, int breedId, DryStorage dryStorage) {
        UserCompany company = userCompanyService.findById(userId);
        BreedOfTree breed = breedOfTreeService.findById(breedId);
        dryStorage.setBreedOfTree(breed);
        dryStorage.setUserCompany(company);
        return save(dryStorage);
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
    public List<DryStorage> getFilteredList(int breedId, int userId, String[] descriptions, String[] heights, String[] longs, String[] widths) {
        List<String> descList, heightList,widthList, longList;
        if (descriptions==null || descriptions.length==0){
            descList = getListOfUnicBreedDescription(breedId);
        }else {
            descList = Arrays.asList(descriptions);
        }
        if (heights==null || heights.length==0){
            heightList = getListOfUnicSizeOfHeight(breedId);
        }else {
            heightList = Arrays.asList(heights);
        }
        if (widths==null || widths.length==0){
            widthList = getListOfUnicSizeOfWidth(breedId);
        }else {
            widthList = Arrays.asList(widths);
        }
        if (longs==null || longs.length==0){
            longList = getListOfUnicSizeOfLong(breedId);
        }else {
            longList = Arrays.asList(longs);
        }
        System.out.println(descList);
        System.out.println(heightList);
        System.out.println(widthList);
        System.out.println(longList);
        if(breedId!=2) {
            return dryStorageJPA.getListByUserByBreed(breedId, userId, descList, heightList, widthList, longList);
        }else {
            return dryStorageJPA.getListByUserByBreedOak(breedId, userId, descList, heightList, longList);
        }
    }

    @Override
    public DryStorage countExtentRawStorageWithDeskDescription(DryStorage dryStorage) {
        if(dryStorage.getDeskOakList()==null || dryStorage.getDeskOakList().size()==0){
            return dryStorage;
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
            DryingStorage dryingStorage = dryStorage.getDryingStorage();
            if(dryingStorage!=null) {
                dryingStorage.setExtent(
                        String.format("%.3f",
                                Double.parseDouble(dryingStorage.getExtent()) +
                                        (Double.parseDouble(dryStorage.getExtent()) - extent))
                                .replace(",", ".")

                );
                dryingStorageService.save(dryingStorage);
            }
        }
        dryStorage.setExtent(
                String.format("%.3f",extent).replace(",",".")
        );
        return dryStorageJPA.save(dryStorage);
    }

    @Override
    public BigDecimal countExtent(List<DryStorage> dryStorage) {
        double sum=0;
        for(DryStorage storage : dryStorage){
            sum += Double.parseDouble(storage.getExtent());
        }
        return new BigDecimal(sum);
    }

    @Override
    public DryStorage editDryStorage(DryStorage dryStorage) {
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

            if(dryingStorage!=null) {
                dryingStorage.setCountOfDesk(dryingStorage.getCountOfDesk() + difExtentDesk);
            }
        }else{
            float difExtent = Float.parseFloat(dryStorageDB.getExtent())-Float.parseFloat(dryStorage.getExtent());

            dryStorageDB.setCodeOfProduct(dryStorage.getCodeOfProduct());
            dryStorageDB.setBreedDescription(dryStorage.getBreedDescription());

            dryStorageDB.setSizeOfHeight(dryStorage.getSizeOfHeight());
            dryStorageDB.setSizeOfLong(dryStorage.getSizeOfLong());
            dryStorageDB.setExtent(dryStorage.getExtent());
            dryStorageDB.setDescription(dryStorage.getDescription());

            if(dryingStorage!=null) {
                dryingStorage.setExtent(
                        String.format("%.3f", Float.parseFloat(dryingStorage.getExtent()) + difExtent)
                                .replace(",", ".")
                );
            }
            countExtentRawStorageWithDeskDescription(dryStorageDB);
        }
        if(dryingStorage!=null) {
            if (dryingStorage.getBreedDescription().codePoints().allMatch(Character::isWhitespace)) {
                dryingStorage.setBreedDescription("");
            }
            dryingStorageService.save(dryingStorage);
        }
        return save(dryStorageDB);
    }

    @Override
    public void deleteByID(int id) {
        dryStorageJPA.deleteById(id);
    }

    @Override
    public DryStorage collectToOnePineEntityDry(DryStorage dryStorage, Integer[] arrOfEntity, int userId, int breedId) {
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
        return save(dryStorage);
    }

    @Override
    public DryStorage collectToOneOakEntityDry(DryStorage dryStorage, Integer[] arrOfEntity, int userId, int breedId) {
        Set<String> width = new TreeSet<>();
        List<DryStorage> rawsFromDBList=dryStorageJPA.findAllById(Arrays.asList(arrOfEntity));
        if(rawsFromDBList.size()>0 && rawsFromDBList.get(0).getDeskOakList().size()==0){
            System.out.println(rawsFromDBList.get(0).getDeskOakList());
            return collectToOnePineEntityDry(dryStorage,arrOfEntity,userId,breedId);

        }
        for(DryStorage temp:rawsFromDBList){
            if(temp!=null && temp.getDeskOakList().size()>0){
                for(DescriptionDeskOak deskOak:temp.getDeskOakList()) {
                    width.add(deskOak.getSizeOfWidth());
                }
                temp.setStatusOfEntity(StatusOfEntity.GROUPED_BY_PROPERTIES);
            }
        }
        dryStorageJPA.saveAll(rawsFromDBList);
        List<DescriptionDeskOak> deskOakList=new ArrayList<>();
        for(String widthOfDesk:width) {
            int countOfDesk = 0;
            for(DryStorage temp:rawsFromDBList){
                if (temp != null && temp.getDeskOakList().size() > 0) {
                    for (DescriptionDeskOak deskOak : temp.getDeskOakList()) {
                        if(deskOak.getSizeOfWidth().equals(widthOfDesk)){
                            countOfDesk+=Integer.parseInt(deskOak.getCountOfDesk());
                        }
                    }
                }
            }
            DescriptionDeskOak desk = new DescriptionDeskOak(widthOfDesk,String.valueOf(countOfDesk));
            desk.setDryStorage(dryStorage);
            deskOakList.add(desk);
        }

        dryStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        dryStorage.setUserCompany(userCompanyService.findById(userId));
        dryStorage.setGroupedElements(rawsFromDBList);
        dryStorage.setDeskOakList(deskOakList);
        return save(dryStorage);
    }

    @Override
    public List<DryStorage> uncollectFromOnePineEntityDry(DryStorage dryStorage, int userId, int breedId) {
        List<DryStorage> list = new ArrayList<>();
        if(dryStorage.getGroupedElements()!=null && dryStorage.getGroupedElements().size()>0){
            for(DryStorage grouped:dryStorage.getGroupedElements()){
                grouped.setStatusOfEntity(StatusOfEntity.ACTIVE);
                list.add(save(grouped));
            }
            deleteByID(dryStorage.getId());
        }
        return list;
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
            return dryStorageJPA.getExtentOak(breedId,breedDesc,sizeHeight,sizeLong,agentId);
        }
        return dryStorageJPA.getExtent(breedId,breedDesc,sizeHeight,sizeWidth,sizeLong,agentId);
    }

    public List<DryStorage> sortedBy(List<DryStorage> list,String sortedField, String sortedType){
        Comparator<DryStorage> comparator = (o1, o2) -> o2.getId()-o1.getId();
        if(sortedField!=null && sortedType!=null) {
            switch (sortedField) {
                case "date":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparing(DryStorage::getDate);
                    } else {
                        comparator = (o1, o2) -> o2.getDate().compareTo(o1.getDate());
                    }
                    break;
                case "code":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparing(DryStorage::getCodeOfProduct);
                    } else {
                        comparator = (o1, o2) -> o2.getCodeOfProduct().compareTo(o1.getCodeOfProduct());
                    }
                    break;
                case "breedDescription":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparing(DryStorage::getBreedDescription);
                    } else {
                        comparator = (o1, o2) -> o2.getBreedDescription().compareTo(o1.getBreedDescription());
                    }
                    break;
                case "height":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparingDouble(o -> Double.parseDouble(o.getSizeOfHeight()));
                    } else {
                        comparator = (o1, o2) -> Double.compare(Double.parseDouble(o2.getSizeOfHeight()), Double.parseDouble(o1.getSizeOfHeight()));
                    }
                    break;
                case "long":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparingDouble(o -> Double.parseDouble(o.getSizeOfLong()));
                    } else {
                        comparator = (o1, o2) -> Double.compare(Double.parseDouble(o2.getSizeOfLong()), Double.parseDouble(o1.getSizeOfLong()));
                    }
                    break;
                case "extent":
                    if (sortedType.equals("ASC")) {
                        comparator = Comparator.comparingDouble(o -> Double.parseDouble(o.getExtent()));
                    } else {
                        comparator = (o1, o2) -> Double.compare(Double.parseDouble(o2.getExtent()), Double.parseDouble(o1.getExtent()));
                    }
                    break;
            }
        }
        list.sort(comparator);
        return list;
    }
}
