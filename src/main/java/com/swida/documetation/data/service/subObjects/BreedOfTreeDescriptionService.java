package com.swida.documetation.data.service.subObjects;

import com.swida.documetation.data.entity.subObjects.BreedOfTreeDescription;

import java.util.List;

public interface BreedOfTreeDescriptionService {
    void save(BreedOfTreeDescription desc);
    BreedOfTreeDescription findById(int id);
    BreedOfTreeDescription getObjectByName (String name);
    List<BreedOfTreeDescription> findAll();
    void deleteByID(int id);
}
