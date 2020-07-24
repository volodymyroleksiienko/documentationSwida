package com.swida.documetation.data.serviceImpl;

import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.jpa.storagesJPA.RawStorageJPA;
import com.swida.documetation.data.service.storagesService.RawStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RawStorageServiceImpl implements RawStorageService {
    RawStorageJPA rawStorageJPA;

    @Autowired
    public RawStorageServiceImpl(RawStorageJPA rawStorageJPA) {
        this.rawStorageJPA = rawStorageJPA;
    }

    @Override
    public void save(RawStorage rs) {
        rawStorageJPA.save(rs);
    }

    @Override
    public RawStorage findById(int id) {
        return rawStorageJPA.getOne(id);
    }

    @Override
    public List<RawStorage> findAll() {
        return rawStorageJPA.findAll();
    }

    @Override
    public void deleteByID(int id) {
        rawStorageJPA.deleteById(id);
    }
}
