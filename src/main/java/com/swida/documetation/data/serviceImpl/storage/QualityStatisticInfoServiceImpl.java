package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.dto.StorageItem;
import com.swida.documetation.data.dto.TreeStorageListDto;
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
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class QualityStatisticInfoServiceImpl implements QualityStatisticInfoService {
    private final QualityStatisticInfoJPA infoJPA;
    private final RawStorageService rawStorageService;
    private final TreeStorageService treeStorageService;
    private final EntityManager entityManager;

    @Autowired
    public QualityStatisticInfoServiceImpl(QualityStatisticInfoJPA infoJPA, @Lazy RawStorageService rawStorageService, @Lazy TreeStorageService treeStorageService, EntityManager entityManager) {
        this.infoJPA = infoJPA;
        this.rawStorageService = rawStorageService;
        this.treeStorageService = treeStorageService;
        this.entityManager = entityManager;
    }

    @Override
    public QualityStatisticInfo save(QualityStatisticInfo info) {
        return infoJPA.save(info);
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
        if(info!=null) {
            RawStorage rawStorage = info.getRawStorage();
            TreeStorage treeStorage = info.getTreeStorage();

            if (rawStorage.getBreedOfTree().getId() == 2) {
                rawStorage.setExtent(
                        String.format("%.3f", Double.parseDouble(rawStorage.getExtent()) - Double.parseDouble(info.getExtent()))
                                .replace(",", ".")
                );
            } else {
                if (info.getCountOfDesk() == null) return;
                rawStorage.setCountOfDesk(
                        rawStorage.getCountOfDesk() - info.getCountOfDesk()
                );
            }
            treeStorage.setExtent(
                    String.format("%.3f", Double.parseDouble(treeStorage.getExtent()) + Double.parseDouble(info.getFirstExtent()))
                            .replace(",", ".")
            );
            rawStorage.setStatisticInfo(null);
            rawStorageService.save(rawStorage);
            List<QualityStatisticInfo> list = treeStorage.getStatisticInfoList();
            list.remove(info);
            treeStorage.setStatisticInfoList(list);
            treeStorageService.save(treeStorage);
            infoJPA.deleteById(info.getId());
        }
    }

    public void edit(QualityStatisticInfo info){
        QualityStatisticInfo infoDB  = findById(info.getId());
        if(infoDB.getCountOfDesk().equals(info.getCountOfDesk()) &&
            infoDB.getHeight().equals(info.getHeight()) &&
            infoDB.getSizeOfWidth().equals(info.getSizeOfWidth()) &&
            infoDB.getSizeOfLong().equals(info.getSizeOfLong()) &&
            infoDB.getBreedDescription().equals(info.getBreedDescription()) ){
            infoDB.setDate(info.getDate());
            infoDB.setCodeOfTeam(info.getCodeOfTeam());
            save(infoDB);
        }else{
            returnQualityInfo(info.getId());
            TreeStorageListDto  dto = new TreeStorageListDto();
            dto.setBreedId(infoDB.getTreeStorage().getBreedOfTree().getId());
            dto.setUserId(infoDB.getTreeStorage().getUserCompany().getId());
            dto.setCodeOfProduct(info.getCodeOfTeam());
            dto.setExtent(Double.parseDouble(infoDB.getFirstExtent()));


            StorageItem storageItem = new StorageItem();
            if(info.getCountOfDesk()!=null) {
                storageItem.setCountOfDesk(info.getCountOfDesk());
            }
            storageItem.setDescription(info.getBreedDescription());
            storageItem.setSizeOfHeight(Integer.parseInt(info.getHeight()));
            if(info.getSizeOfWidth()!=null) {
                storageItem.setSizeOfWidth(Integer.parseInt(info.getSizeOfWidth()));
            }
            storageItem.setSizeOfLong(Integer.parseInt(info.getSizeOfLong()));
            if(info.getSizeOfWidth()!=null && Integer.parseInt(info.getSizeOfWidth())>0) {
                double extent = Double.parseDouble(info.getHeight()) * Double.parseDouble(info.getSizeOfWidth()) *
                        Double.parseDouble(info.getSizeOfLong()) * info.getCountOfDesk() / 1000000000;
                storageItem.setExtent(extent);
            }else {
                storageItem.setExtent(Double.parseDouble(info.getExtent()));
            }
            dto.setStorageItems(Collections.singletonList(storageItem));
            rawStorageService.analyzeOfCutting(dto);
        }
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
        System.out.println("delete "+id);
        infoJPA.deleteQualityStatisticInfoById(id);
    }
}
