package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.QualityStatisticInfo;

import java.util.List;

public interface QualityStatisticInfoService {
    void save(QualityStatisticInfo info);
    QualityStatisticInfo findById(int id);
    List<QualityStatisticInfo> findAll();
    void deleteByID(int id);
}
