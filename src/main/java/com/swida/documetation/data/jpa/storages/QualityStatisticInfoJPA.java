package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QualityStatisticInfoJPA extends JpaRepository<QualityStatisticInfo,Integer> {
    QualityStatisticInfo findByTreeStorageIdAndAndHeight(int id,String height);
}
