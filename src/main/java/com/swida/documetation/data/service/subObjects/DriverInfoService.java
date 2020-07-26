package com.swida.documetation.data.service.subObjects;

import com.swida.documetation.data.entity.subObjects.DriverInfo;

import java.util.List;

public interface DriverInfoService {
    void save(DriverInfo driverInfo);
    DriverInfo findById(int id);
    List<DriverInfo> findAll();
    void deleteByID(int id);
}
