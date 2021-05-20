package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.jpa.storages.QualityStatisticInfoJPA;
import com.swida.documetation.data.service.storages.QualityStatisticInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualityStatisticInfoServiceImpl implements QualityStatisticInfoService {
    private QualityStatisticInfoJPA infoJPA;

    @Autowired
    public QualityStatisticInfoServiceImpl(QualityStatisticInfoJPA infoJPA) {
        this.infoJPA = infoJPA;
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

    public QualityStatisticInfo createObject(RawStorage rawStorage){
        return null;
    }

    @Override
    public List<QualityStatisticInfo> findByUserByBreedByHeightByDescription(List<Integer> userId, List<Integer> breedId, List<String> heights, List<String> description) {
        System.out.println(userId);
        System.out.println(breedId);
        System.out.println(heights);
        System.out.println(description);
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
