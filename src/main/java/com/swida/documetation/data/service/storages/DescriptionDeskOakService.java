package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.storages.DescriptionDeskOak;

import java.util.List;

public interface DescriptionDeskOakService {
    void save(DescriptionDeskOak descriptionDesk);
    void saveAll(List<DescriptionDeskOak> list);
    DescriptionDeskOak findById(int id);
    List<DescriptionDeskOak> findAll();
    void deleteByID(int id);
}
