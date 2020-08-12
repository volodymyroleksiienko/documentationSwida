package com.swida.documetation.data.service.subObjects;

import com.swida.documetation.data.entity.subObjects.Container;

import java.util.List;

public interface ContainerService {
    void save(Container container);
    Container findById(int id);
    List<Container> findAll();
    void deleteByID(int id);
}
