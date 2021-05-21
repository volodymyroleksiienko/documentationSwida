package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.jpa.storages.QualityStatisticInfoJPA;
import com.swida.documetation.data.service.storages.QualityStatisticInfoService;
import com.swida.documetation.data.service.storages.RawStorageService;
import com.swida.documetation.data.service.storages.TreeStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class QualityStatisticInfoServiceImpl implements QualityStatisticInfoService {
    private QualityStatisticInfoJPA infoJPA;
    private RawStorageService rawStorageService;
    private TreeStorageService treeStorageService;

    @Autowired
    public QualityStatisticInfoServiceImpl(QualityStatisticInfoJPA infoJPA, @Lazy RawStorageService rawStorageService,@Lazy TreeStorageService treeStorageService) {
        this.infoJPA = infoJPA;
        this.rawStorageService = rawStorageService;
        this.treeStorageService = treeStorageService;
    }

    @Override
    public void save(QualityStatisticInfo info) {
        infoJPA.save(info);
    }

    @Override
    public QualityStatisticInfo findById(int id) {
        return infoJPA.getOne(id);
    }

    @Override
    public List<QualityStatisticInfo> findByUserByBreed(List<Integer> userId, List<Integer> breedId) {
        return infoJPA.findByUserByBreed(userId,breedId);
    }

    @Override
    public QualityStatisticInfo findByTreeStorageIdAndAndHeight(int id, String height) {
        return infoJPA.findByTreeStorageIdAndAndHeight(id,height);
    }

    @Override
    public void returnQualityInfo(int id){
        QualityStatisticInfo info = findById(id);
        RawStorage rawStorage = info.getRawStorage();
        TreeStorage treeStorage = info.getTreeStorage();

        if(rawStorage.getBreedOfTree().getId()==2){
            rawStorage.setExtent(
                    String.format("%.3f",Double.parseDouble(rawStorage.getExtent())-Double.parseDouble(info.getExtent()))
                            .replace(",",".")
            );
        }else{
            if(info.getCountOfDesk()==null) return;
            rawStorage.setCountOfDesk(
                    rawStorage.getCountOfDesk()+info.getCountOfDesk()
            );
        }
        treeStorage.setExtent(
                String.format("%.3f",Double.parseDouble(rawStorage.getExtent())+Double.parseDouble(info.getFirstExtent()))
                        .replace(",",".")
        );
        rawStorageService.save(rawStorage);
        treeStorageService.save(treeStorage);
        deleteByID(info.getId());
    }

    @Override
    public List<QualityStatisticInfo> findByUserByBreedByHeightByDescription(List<Integer> userId, List<Integer> breedId, List<String> heights, List<String> description) {
        return infoJPA.findByUserByBreedByHeightByDescription(userId,breedId,heights,description);
    }

    @Override
    public List<QualityStatisticInfo> findAll() {
        return infoJPA.findAll();
    }

    @Override
    public void deleteByID(int id) {
        infoJPA.deleteById(id);
    }
}
