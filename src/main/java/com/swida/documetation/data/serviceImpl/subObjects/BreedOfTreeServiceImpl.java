package com.swida.documetation.data.serviceImpl.subObjects;

import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.jpa.subObjects.BreedOfTreeJPA;
import com.swida.documetation.data.service.subObjects.BreedOfTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BreedOfTreeServiceImpl implements BreedOfTreeService {
    BreedOfTreeJPA breedOfTreeJPA;

    @Autowired
    public BreedOfTreeServiceImpl(BreedOfTreeJPA breedOfTreeJPA) {
        this.breedOfTreeJPA = breedOfTreeJPA;
    }

    @Override
    public void save(BreedOfTree breedOfTree) {
        breedOfTreeJPA.save(breedOfTree);
    }

    @Override
    public BreedOfTree findById(int id) {
        return breedOfTreeJPA.getOne(id);
    }

    @Override
    public List<BreedOfTree> findAll() {
        return breedOfTreeJPA.findAll();
    }

    @Override
    public void deleteByID(int id) {
        breedOfTreeJPA.deleteById(id);
    }
}
