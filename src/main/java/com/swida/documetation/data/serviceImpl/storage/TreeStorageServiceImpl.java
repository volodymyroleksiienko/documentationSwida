package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import com.swida.documetation.data.jpa.storages.TreeStorageJPA;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.QualityStatisticInfoService;
import com.swida.documetation.data.service.storages.RawStorageService;
import com.swida.documetation.data.service.storages.TreeStorageService;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
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
    private final TreeStorageJPA treeStorageJPA;
    private final ContrAgentService contrAgentService;
    private final RawStorageService rawStorageService;
    private final BreedOfTreeService breedOfTreeService;
    private final UserCompanyService userCompanyService;
    private final QualityStatisticInfoService statisticInfoService;

    @Autowired
    public TreeStorageServiceImpl(TreeStorageJPA treeStorageJPA, ContrAgentService contrAgentService,
                                  @Lazy RawStorageService rawStorageService, BreedOfTreeService breedOfTreeService,
                                  UserCompanyService userCompanyService, QualityStatisticInfoService statisticInfoService) {
        this.treeStorageJPA = treeStorageJPA;
        this.contrAgentService = contrAgentService;
        this.rawStorageService = rawStorageService;
        this.breedOfTreeService = breedOfTreeService;
        this.userCompanyService = userCompanyService;
        this.statisticInfoService = statisticInfoService;
    }


    @Override
    public TreeStorage save(TreeStorage ts) {
        if(ts.getDate()==null){
            Date date = new Date(System.currentTimeMillis());
            ts.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
        if(ts.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            ts.setBreedDescription("");
        }
        ts.setExtent(String.format("%.3f", Float.parseFloat(ts.getExtent())).replace(',', '.'));
        return treeStorageJPA.save(ts);
    }

    @Override
    public TreeStorage putNewTreeStorageObj(int breedId, int userId,TreeStorage treeStorage) {
        treeStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
        treeStorage.setUserCompany(userCompanyService.findById(userId));
        treeStorage.setExtent(String.format("%.3f", Float.parseFloat(treeStorage.getExtent())).replace(',', '.'));
        treeStorage.setMaxExtent(treeStorage.getExtent());
        if(treeStorage.getDate()==null){
            Date date = new Date(System.currentTimeMillis());
            treeStorage.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
        if(treeStorage.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            treeStorage.setBreedDescription("");
        }
        treeStorage.setExtent(String.format("%.3f", Float.parseFloat(treeStorage.getExtent())).replace(',', '.'));
        checkMainStorageExtent(treeStorage);
        return treeStorageJPA.save(treeStorage);
    }

    public TreeStorage checkMainStorageExtent(TreeStorage treeStorage){
        int breedId = treeStorage.getBreedOfTree().getId();
        int userId = treeStorage.getUserCompany().getId();
        if( breedId!=0 && userId!=0) {
            TreeStorage main = getMainTreeStorage(breedId, userId);
            double extent = 0;
            if(treeStorage.getId()==0) {
                extent = Double.parseDouble(main.getExtent()) + Double.parseDouble(treeStorage.getExtent());

            }else {
                TreeStorage treeStorageDB = findById(treeStorage.getId());
                extent = Double.parseDouble(main.getExtent())
                        + Double.parseDouble(treeStorage.getExtent())
                        - Double.parseDouble(treeStorageDB.getExtent());
            }
            main.setExtent(
                    String.format("%.3f", extent).replace(",", ".")
            );
            return save(main);
        }
        return null;
    }

    @Override
    public TreeStorage getMainTreeStorage(int breedId, int userId){
        System.out.println("breedId " +breedId);
        System.out.println("userId " +userId);
        TreeStorage treeStorage = treeStorageJPA.getListByUserByBreedByMain(breedId,userId,StatusOfTreeStorage.TREE,true).orElse(null);
        if(treeStorage == null){
            treeStorage = new TreeStorage();
            treeStorage.setIsMainStorage(true);
            treeStorage.setBreedOfTree(breedOfTreeService.findById(breedId));
            treeStorage.setUserCompany(userCompanyService.findById(userId));
            treeStorage.setCodeOfProduct("Остаток всего");

            double extent = 0;
            List<TreeStorage> treeStorageList = treeStorageJPA.getListByUserByBreed(breedId,userId,StatusOfTreeStorage.TREE);
            for(TreeStorage temp:treeStorageList){
                extent+=Double.parseDouble(temp.getExtent());
            }
            treeStorage.setExtent(
                    String.format("%.3f",extent).replace(",",".")
            );
            treeStorage = save(treeStorage);
        }
        return treeStorage;
    }

    @Override
    public List<TreeStorage> getMainTreeStorage(int breedId, List<Integer> userId) {
        return treeStorageJPA.getListByUserByBreedByMain(breedId,userId,StatusOfTreeStorage.TREE,true);
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
                .filter(treeStorage -> treeStorage.getIsMainStorage()==null || !treeStorage.getIsMainStorage())
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
