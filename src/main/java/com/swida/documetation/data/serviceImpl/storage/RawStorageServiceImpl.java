package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.jpa.storages.RawStorageJPA;
import com.swida.documetation.data.service.storages.RawStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        if (rs.getSizeOfWidth()!=null){
            float width = Float.parseFloat(rs.getSizeOfWidth())/1000;
            float height = Float.parseFloat(rs.getSizeOfHeight())/1000;
            float longSize = Float.parseFloat(rs.getSizeOfLong())/1000;
            int count = rs.getCountOfDesk();
            float extent = width*height*longSize*count;
            rs.setExtent(String.valueOf(extent));
        }
        Date date = new Date(System.currentTimeMillis());
        rs.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
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
    public List<RawStorage> getListByUserByBreed(int breedId, int userId) {
        return rawStorageJPA.getListByUserByBreed(breedId,userId);
    }

    @Override
    public void deleteByID(int id) {
        rawStorageJPA.deleteById(id);
    }
}
