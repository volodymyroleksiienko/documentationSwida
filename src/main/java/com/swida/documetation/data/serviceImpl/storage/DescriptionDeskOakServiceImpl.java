package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.DescriptionDeskOak;
import com.swida.documetation.data.jpa.storages.DescriptionDeskOakJPA;
import com.swida.documetation.data.jpa.storages.DryingStorageJPA;
import com.swida.documetation.data.service.storages.DescriptionDeskOakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DescriptionDeskOakServiceImpl implements DescriptionDeskOakService {
    private DescriptionDeskOakJPA descriptionDeskOakJPA;

    @Autowired
    public DescriptionDeskOakServiceImpl(DescriptionDeskOakJPA descriptionDeskOakJPA) {
        this.descriptionDeskOakJPA = descriptionDeskOakJPA;
    }

    @Override
    public void save(DescriptionDeskOak descriptionDesk) {
        descriptionDeskOakJPA.save(descriptionDesk);
    }

    @Override
    public void saveAll(List<DescriptionDeskOak> list) {
        descriptionDeskOakJPA.saveAll(list);
    }

    @Override
    public void editDescription(String deskId, String width, String count) {
        DescriptionDeskOak deskOak = descriptionDeskOakJPA.getOne(Integer.parseInt(deskId));
        deskOak.setSizeOfWidth(width);
        deskOak.setCountOfDesk(count);
        descriptionDeskOakJPA.save(deskOak);
    }

    @Override
    public DescriptionDeskOak findById(int id) {
        return descriptionDeskOakJPA.getOne(id);
    }

    @Override
    public List<DescriptionDeskOak> findAll() {
        return descriptionDeskOakJPA.findAll(Sort.by(Sort.Direction.DESC,"id"));
    }



    @Override
    public void deleteByID(int id) {
        DescriptionDeskOak desk = findById(id);
        if(desk!=null) {
            desk.setRawStorage(null);
            save(desk);
            descriptionDeskOakJPA.deleteById(id);
        }
    }
}
