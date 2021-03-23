package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.DescriptionDeskOak;
import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.enums.StatusOfEntity;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import com.swida.documetation.data.jpa.storages.RawStorageJPA;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.RawStorageService;
import com.swida.documetation.data.service.storages.TreeStorageService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RawStorageServiceImpl implements RawStorageService {
    private final RawStorageJPA rawStorageJPA;
    private final TreeStorageService treeStorageService;
    private final BreedOfTreeService breedOfTreeService;
    private final UserCompanyService userCompanyService;



    @Override
    public String save(RawStorage rs) {
        if (rs.getSizeOfWidth()!=null){
            float width = Float.parseFloat(rs.getSizeOfWidth())/1000;
            float height = Float.parseFloat(rs.getSizeOfHeight())/1000;
            float longSize = Float.parseFloat(rs.getSizeOfLong())/1000;
            int count = rs.getCountOfDesk();
            float extent = width*height*longSize*count;
            rs.setExtent(String.format("%.3f",extent).replace(',','.'));
        }
        rs.setExtent(String.format("%.3f", Float.parseFloat(rs.getExtent())).replace(',', '.'));
        Date date = new Date(System.currentTimeMillis());
        rs.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        if(rs.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            rs.setBreedDescription("");
        }
        rawStorageJPA.save(rs);
        return rs.getExtent();
    }

    @Override
    public RawStorage findById(int id) {
        return rawStorageJPA.getOne(id);
    }

    @Override
    public List<RawStorage> findAll() {
        return rawStorageJPA.findAll();
    }

    @Override
    public List<RawStorage> findAllByTreeStorageId(int id) {
        return rawStorageJPA.findAllByTreeStorageId(id);
    }

    @Override
    public List<RawStorage> getListByUserByBreed(int breedId, int userId) {
        if (breedId==2){
            return rawStorageJPA.getListByUserByBreedOak(breedId,userId);
        }
        return rawStorageJPA.getListByUserByBreed(breedId,userId);
    }

    @Override
    public List<RawStorage> getListByUserByBreedByStatusOfTree(int breedId, int userId, StatusOfTreeStorage status) {
        return rawStorageJPA.getListByUserByBreedByStatusOfTree(breedId,userId,status)
                .stream()
                .sorted((o1, o2) -> o2.getId()-o1.getId())
                .collect(Collectors.toList());
    }

    @Override
    public void countExtentRawStorageWithDeskDescription(RawStorage rawStorage) {
        if(rawStorage.getDeskOakList()==null || rawStorage.getDeskOakList().size()==0){
            return;
        }
        float extent = 0;
        for(DescriptionDeskOak deskOak:  rawStorage.getDeskOakList()){
            extent+= (Double.parseDouble(deskOak.getSizeOfWidth())
                    *Double.parseDouble(deskOak.getCountOfDesk())
                    *Double.parseDouble(rawStorage.getSizeOfHeight())
                    *Double.parseDouble(rawStorage.getSizeOfLong())
                    /1000000000);
        }
        if (Double.parseDouble(rawStorage.getExtent())!=extent){
            TreeStorage treeStorage = rawStorage.getTreeStorage();
            treeStorage.setExtent(
                    String.format("%.3f",
                            Double.parseDouble(treeStorage.getExtent())+
                                    (Double.parseDouble(rawStorage.getExtent())-extent))
                            .replace(",",".")

            );
            treeStorageService.checkQualityInfo(treeStorage,rawStorage.getSizeOfHeight(),extent-Float.parseFloat(rawStorage.getExtent()));
            treeStorageService.save(treeStorage);
        }
        rawStorage.setExtent(
                String.format("%.3f",extent).replace(",",".")
        );
       rawStorageJPA.save(rawStorage);
    }

    public void collectToOnePineEntity(RawStorage rawStorage,Integer[] arrOfEntity,int userId,int breedId){
        int countOfDesk = 0;
        double extend=0;
        List<RawStorage> groupedList = new ArrayList<>();
        for(Integer id:arrOfEntity){
            RawStorage temp =rawStorageJPA.findById(id).orElse(null);
            if(temp!=null) {
                if(breedId==1) {
                    countOfDesk += temp.getCountOfDesk();
                }else{
                    extend+=Double.parseDouble(temp.getExtent());
                }
                groupedList.add(temp);
                temp.setStatusOfEntity(StatusOfEntity.GROUPED_BY_PROPERTIES);
                rawStorageJPA.save(temp);
            }
        }

        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setGroupedElements(groupedList);
        rawStorage.setCountOfDesk(countOfDesk);
        System.out.println(rawStorage);
        String mainExtend = save(rawStorage);
        rawStorage.setMaxExtent(mainExtend);
        save(rawStorage);
    }

    @Override
    public void uncollectFromOnePineEntity(RawStorage rawStorage, int userId, int breedId) {
        if(rawStorage.getGroupedElements()!=null && rawStorage.getGroupedElements().size()>0){
            for(RawStorage grouped:rawStorage.getGroupedElements()){
                grouped.setStatusOfEntity(StatusOfEntity.ACTIVE);
                save(grouped);
            }
            deleteByID(rawStorage.getId());
        }
    }

    @Override
    public void collectToOneOakEntity(RawStorage rawStorage, Integer[] arrOfEntity, int userId, int breedId) {
        Set<String> width = new TreeSet<>();
        List<RawStorage> rawsFromDBList=rawStorageJPA.findAllById(Arrays.asList(arrOfEntity));
        if(rawsFromDBList.size()>0 && rawsFromDBList.get(0).getDeskOakList().size()==0){
            System.out.println(rawsFromDBList.get(0).getDeskOakList());
            collectToOnePineEntity(rawStorage,arrOfEntity,userId,breedId);
            return;
        }
        for(RawStorage temp:rawsFromDBList){
            if(temp!=null && temp.getDeskOakList().size()>0){
                for(DescriptionDeskOak deskOak:temp.getDeskOakList()) {
                    width.add(deskOak.getSizeOfWidth());
                }
                temp.setStatusOfEntity(StatusOfEntity.GROUPED_BY_PROPERTIES);
            }
        }
        rawStorageJPA.saveAll(rawsFromDBList);
        List<DescriptionDeskOak> deskOakList=new ArrayList<>();
        for(String widthOfDesk:width) {
            int countOfDesk = 0;
            for(RawStorage temp:rawsFromDBList){
                if (temp != null && temp.getDeskOakList().size() > 0) {
                    for (DescriptionDeskOak deskOak : temp.getDeskOakList()) {
                        if(deskOak.getSizeOfWidth().equals(widthOfDesk)){
                            countOfDesk+=Integer.parseInt(deskOak.getCountOfDesk());
                        }
                    }
                }
            }
            DescriptionDeskOak desk = new DescriptionDeskOak(widthOfDesk,String.valueOf(countOfDesk));
            desk.setRawStorage(rawStorage);
            deskOakList.add(desk);
        }

        rawStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        rawStorage.setUserCompany(userCompanyService.findById(userId));
        rawStorage.setGroupedElements(rawsFromDBList);
        rawStorage.setDeskOakList(deskOakList);
        String extend = save(rawStorage);
        rawStorage.setMaxExtent(extend);
        save(rawStorage);
    }


    @Override
    public void deleteByID(int id) {
        rawStorageJPA.deleteById(id);
    }

    @Override
    public List<String> getListOfUnicBreedDescription(int breedId) {
        return rawStorageJPA.getListOfUnicBreedDescription(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfHeight(int breedId) {
        return rawStorageJPA.getListOfUnicSizeOfHeight(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfWidth(int breedId) {
        return rawStorageJPA.getListOfUnicSizeOfWidth(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfLong(int breedId) {
        return rawStorageJPA.getListOfUnicSizeOfLong(breedId);
    }

    @Override
    public List<String> getExtent(int breedId, String[] breedDesc, String[] sizeHeight, String[] sizeWidth, String[] sizeLong, int[] agentId) {
        if(breedId==2){
            return rawStorageJPA.getExtentOak(breedId,breedDesc,sizeHeight,agentId);
        }
        return rawStorageJPA.getExtent(breedId,breedDesc,sizeHeight,sizeWidth,sizeLong,agentId);
    }
}
