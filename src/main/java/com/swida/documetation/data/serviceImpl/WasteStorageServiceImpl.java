package com.swida.documetation.data.serviceImpl;

import com.swida.documetation.data.entity.storages.WasteStorage;
import com.swida.documetation.data.jpa.storagesJPA.WasteStorageJPA;
import com.swida.documetation.data.service.storagesService.WasteStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WasteStorageServiceImpl implements WasteStorageService {
    WasteStorageJPA wasteStorageJPA;

    @Autowired
    public WasteStorageServiceImpl(WasteStorageJPA wasteStorageJPA) {
        this.wasteStorageJPA = wasteStorageJPA;
    }

    @Override
    public void save(WasteStorage ws) {
        wasteStorageJPA.save(ws);
    }

    @Override
    public WasteStorage findById(int id) {
        return wasteStorageJPA.getOne(id);
    }

    @Override
    public List<WasteStorage> findAll() {
        return wasteStorageJPA.findAll();
    }

    @Override
    public void deleteByID(int id) {
        wasteStorageJPA.deleteById(id);
    }
}
