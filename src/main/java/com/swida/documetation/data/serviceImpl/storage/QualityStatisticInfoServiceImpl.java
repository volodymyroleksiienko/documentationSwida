package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
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
    public List<QualityStatisticInfo> findAll() {
        return infoJPA.findAll();
    }

    @Override
    public void deleteByID(int id) {
        infoJPA.deleteById(id);
    }
}
