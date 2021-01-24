package com.swida.documetation.data.serviceImpl.subObjects;

import com.swida.documetation.data.entity.subObjects.DriverInfo;
import com.swida.documetation.data.jpa.subObjects.DriverInfoJPA;
import com.swida.documetation.data.service.subObjects.DriverInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DriverInfoServiceImpl implements DriverInfoService {
    DriverInfoJPA driverInfoJPA;

    @Autowired
    public DriverInfoServiceImpl(DriverInfoJPA driverInfoJPA) {
        this.driverInfoJPA = driverInfoJPA;
    }

    @Override
    public void save(DriverInfo driverInfo) {
        driverInfoJPA.save(driverInfo);
    }

    @Override
    public DriverInfo findById(int id) {
        return driverInfoJPA.getOne(id);
    }

    @Override
    public List<DriverInfo> findAll() {
        return driverInfoJPA.findAll(Sort.by(Sort.Direction.ASC,"id"));
    }

    @Override
    public void deleteByID(int id) {
        driverInfoJPA.deleteById(id);
    }
}
