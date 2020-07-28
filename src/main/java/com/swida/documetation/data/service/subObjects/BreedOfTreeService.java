package com.swida.documetation.data.service.subObjects;

import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;

import java.util.List;

public interface BreedOfTreeService {
    void save(BreedOfTree breedOfTree);
    BreedOfTree findById(int id);
    List<BreedOfTree> findAll();
    void deleteByID(int id);
}
