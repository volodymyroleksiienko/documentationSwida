package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import com.swida.documetation.data.jpa.storages.TreeStorageJPA;
import com.swida.documetation.data.service.storages.QualityStatisticInfoService;
import com.swida.documetation.data.service.storages.RawStorageService;
import com.swida.documetation.data.service.storages.TreeStorageService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TreeStorageServiceImpl implements TreeStorageService {
    private TreeStorageJPA treeStorageJPA;
    private ContrAgentService contrAgentService;
    private RawStorageService rawStorageService;
    private QualityStatisticInfoService statisticInfoService;

    @Autowired
    public TreeStorageServiceImpl(TreeStorageJPA treeStorageJPA, ContrAgentService contrAgentService,
                                  @Lazy RawStorageService rawStorageService, QualityStatisticInfoService statisticInfoService) {
        this.treeStorageJPA = treeStorageJPA;
        this.contrAgentService = contrAgentService;
        this.rawStorageService = rawStorageService;
        this.statisticInfoService = statisticInfoService;
    }


    @Override
    public void save(TreeStorage ts) {
        if(ts.getDate()==null){
            Date date = new Date(System.currentTimeMillis());
            ts.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
        if(ts.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            ts.setBreedDescription("");
        }
       ts.setExtent(String.format("%.3f", Float.parseFloat(ts.getExtent())).replace(',', '.'));
        treeStorageJPA.save(ts);
    }

    @Override
    public void putNewTreeStorageObj(TreeStorage treeStorage) {
        if(treeStorage.getDate()==null){
            Date date = new Date(System.currentTimeMillis());
            treeStorage.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
        if(treeStorage.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            treeStorage.setBreedDescription("");
        }
        treeStorage.setExtent(String.format("%.3f", Float.parseFloat(treeStorage.getExtent())).replace(',', '.'));
        treeStorageJPA.save(treeStorage);
    }



//    @Override
//    public void checkQualityInfo(TreeStorage treeStorage,String height, float extent) {
//        boolean checkUpdate = true;
//        if(treeStorage.getStatisticInfoList()==null){
//            treeStorage.setStatisticInfoList(new ArrayList<>());
//        }
//        for(QualityStatisticInfo info: treeStorage.getStatisticInfoList()){
//            if(info.getHeight().equals(height)){
//                info.setExtent(
//                        String.format("%.3f",Float.parseFloat(info.getExtent())+extent)
//                        .replace(",",".")
//                );
//                info.setPercent(
//                        String.format("%.3f",Float.parseFloat(info.getExtent())/Float.parseFloat(treeStorage.getMaxExtent())*100)
//                                .replace(",",".")
//                );
//                info.setTreeStorage(treeStorage);
//                checkUpdate = false;
//            }
//        }
//
//        if(checkUpdate){
//            QualityStatisticInfo info = new QualityStatisticInfo();
//            info.setHeight(height);
//            info.setExtent( String.format("%.3f",extent)
//                    .replace(",","."));
//            info.setPercent(
//                    String.format("%.3f",Float.parseFloat(info.getExtent())/Float.parseFloat(treeStorage.getMaxExtent())*100)
//                            .replace(",",".")
//            );
//            info.setTreeStorage(treeStorage);
//            treeStorage.getStatisticInfoList().add(info);
//            statisticInfoService.save(info);
//        }
//        treeStorageJPA.save(treeStorage);
//    }

//    @Override
//    public void checkQualityInfo(TreeStorage treeStorage,String height, float extent) {
//        QualityStatisticInfo info = statisticInfoService.findByTreeStorageIdAndAndHeight(treeStorage.getId(),height);
//        if(info!=null){
//
//        }
//    }


    @Override
    public TreeStorage findById(int id) {
        return treeStorageJPA.getOne(id);
    }

    @Override
    public List<TreeStorage> findAll() {
        return treeStorageJPA.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }

    @Override
    public List<TreeStorage> getListByUserByBreed(int breedId, int userId, StatusOfTreeStorage status) {
        return treeStorageJPA.getListByUserByBreed(breedId,userId,status)
                .stream()
                .sorted((o1, o2) -> o2.getId()-o1.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<TreeStorage> getListByUserByBreedALL(int breedId, int userId, StatusOfTreeStorage status) {
        return treeStorageJPA.getListByUserByBreedALL(breedId,userId,status)
                .stream()
                .sorted((o1, o2) -> o2.getId()-o1.getId())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getListOfUnicBreedDescription(int breedId) {
        return treeStorageJPA.getListOfUnicBreedDescription(breedId);
    }

    @Override
    public List<String> getListOfUnicBreedDescription(int breedId, int userId) {
        return treeStorageJPA.getListOfUnicBreedDescription(breedId,userId);
    }

    @Override
    public Map<Integer, List<QualityStatisticInfo>> getQualityStatisticInfo(List<TreeStorage> treeStorageList) {
        Map<Integer,List<QualityStatisticInfo>> returnedMap= new HashMap<>();
        for (TreeStorage treeStorage: treeStorageList) {
            List<RawStorage> rawStorages = new ArrayList<>();
            Set<String> heightSet = new TreeSet<>();
            List<QualityStatisticInfo> qualityList = new ArrayList<>();
            rawStorages = rawStorageService.findAllByTreeStorageId(treeStorage.getId());
            for(RawStorage rawStorage:rawStorages){
                heightSet.add(rawStorage.getSizeOfHeight());
            }
            for(String heightOfTree:heightSet){
                QualityStatisticInfo qualityStatisticInfo = new QualityStatisticInfo();
                qualityStatisticInfo.setHeight(heightOfTree);
                for(RawStorage rawStorage:rawStorages){
                    if(rawStorage.getSizeOfHeight().equals(qualityStatisticInfo.getHeight())) {
                        qualityStatisticInfo.setExtent(
                                String.format("%.3f", Float.parseFloat(qualityStatisticInfo.getExtent())
                                        + Float.parseFloat(rawStorage.getExtent())).replace(",", ".")
                        );
                    }
                }
                qualityStatisticInfo.setPercent(
                        String.format("%.3f",Float.parseFloat(qualityStatisticInfo.getExtent())/Float.parseFloat(treeStorage.getMaxExtent()))
                                .replace(",",".")
                );
                qualityList.add(qualityStatisticInfo);
            }
            returnedMap.put(treeStorage.getId(),qualityList);
        }
        return returnedMap;
    }

    @Override
    public void deleteByID(int id) {
        treeStorageJPA.deleteById(id);
    }

    @Override
    public List<String> getListOfExtent(int breedId, String[] breedDesc, int[] providers, StatusOfTreeStorage status) {
        return treeStorageJPA.getListOfExtent(breedId,breedDesc,providers,status);
    }

}
