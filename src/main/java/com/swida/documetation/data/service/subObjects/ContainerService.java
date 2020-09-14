package com.swida.documetation.data.service.subObjects;

import com.swida.documetation.data.entity.subObjects.Container;
import com.swida.documetation.data.enums.StatusOfEntity;

import java.util.List;

public interface ContainerService {
    void save(Container container);
    Container findById(int id);
    List<Container> findAll();
    List<Container> selectByStatusOfEntity(StatusOfEntity status);
    String getExtentInContainer(int containerId);
    void setContainerCurrency(String[] containerId, String currency);
    void deleteByID(int id);
}
