package com.swida.documetation.data.service.storages;

import com.swida.documetation.data.entity.storages.DescriptionDeskOak;

import java.util.List;

public interface DescriptionDeskOakService {
    DescriptionDeskOak save(DescriptionDeskOak descriptionDesk);
    void saveAll(List<DescriptionDeskOak> list);
    void editDescription(String deskId, String width, String count);
    DescriptionDeskOak findById(int id);
    List<DescriptionDeskOak> findAll();
    void deleteByID(int id);
}
