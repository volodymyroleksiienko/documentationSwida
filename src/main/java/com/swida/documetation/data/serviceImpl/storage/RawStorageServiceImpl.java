package com.swida.documetation.data.serviceImpl.storage;

import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
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
    public String save(RawStorage rs) {
        if (rs.getSizeOfWidth()!=null){
            float width = Float.parseFloat(rs.getSizeOfWidth())/1000;
            float height = Float.parseFloat(rs.getSizeOfHeight())/1000;
            float longSize = Float.parseFloat(rs.getSizeOfLong())/1000;
            int count = rs.getCountOfDesk();
            float extent = width*height*longSize*count;
            rs.setExtent(String.format("%.3f",extent).replace(',','.'));
        }
        rs.setExtent(String.format("%.3f", Float.parseFloat(rs.getExtent())).replace(',', '.'));
        Date date = new Date(System.currentTimeMillis());
        rs.setDate(new SimpleDateFormat("yyyy-MM-dd").format(date));
        if(rs.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            rs.setBreedDescription("");
        }
        rawStorageJPA.save(rs);
        return rs.getExtent();
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
    public List<RawStorage> findAllByTreeStorageId(int id) {
        return rawStorageJPA.findAllByTreeStorageId(id);
    }

    @Override
    public List<RawStorage> getListByUserByBreed(int breedId, int userId) {
        if (breedId==2){
            return rawStorageJPA.getListByUserByBreedOak(breedId,userId);
        }
        return rawStorageJPA.getListByUserByBreed(breedId,userId);
    }

    @Override
    public List<RawStorage> getListByUserByBreedByStatusOfTree(int breedId, int userId, StatusOfTreeStorage status) {
        return rawStorageJPA.getListByUserByBreedByStatusOfTree(breedId,userId,status);
    }

    @Override
    public void deleteByID(int id) {
        rawStorageJPA.deleteById(id);
    }

    @Override
    public List<String> getListOfUnicBreedDescription(int breedId) {
        return rawStorageJPA.getListOfUnicBreedDescription(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfHeight(int breedId) {
        return rawStorageJPA.getListOfUnicSizeOfHeight(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfWidth(int breedId) {
        return rawStorageJPA.getListOfUnicSizeOfWidth(breedId);
    }

    @Override
    public List<String> getListOfUnicSizeOfLong(int breedId) {
        return rawStorageJPA.getListOfUnicSizeOfLong(breedId);
    }

    @Override
    public List<String> getExtent(int breedId, String[] breedDesc, String[] sizeHeight, String[] sizeWidth, String[] sizeLong, int[] agentId) {
        if(breedId==2){
            return rawStorageJPA.getExtentOak(breedId,breedDesc,sizeHeight,agentId);
        }
        return rawStorageJPA.getExtent(breedId,breedDesc,sizeHeight,sizeWidth,sizeLong,agentId);
    }
}
