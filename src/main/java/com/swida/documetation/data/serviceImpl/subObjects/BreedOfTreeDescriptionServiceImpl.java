package com.swida.documetation.data.serviceImpl.subObjects;

import com.swida.documetation.data.entity.subObjects.BreedOfTreeDescription;
import com.swida.documetation.data.jpa.subObjects.BreedOfTreeDescriptionJPA;
import com.swida.documetation.data.service.subObjects.BreedOfTreeDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BreedOfTreeDescriptionServiceImpl implements BreedOfTreeDescriptionService {
    private BreedOfTreeDescriptionJPA breedOfTreeDescriptionJPA;

    @Autowired
    public BreedOfTreeDescriptionServiceImpl(BreedOfTreeDescriptionJPA breedOfTreeDescriptionJPA) {
        this.breedOfTreeDescriptionJPA = breedOfTreeDescriptionJPA;
    }

    @Override
    public void save(BreedOfTreeDescription desc) {
        breedOfTreeDescriptionJPA.save(desc);
    }

    @Override
    public BreedOfTreeDescription findById(int id) {
        return breedOfTreeDescriptionJPA.getOne(id);
    }

    @Override
    public BreedOfTreeDescription getObjectByName(String name) {
        return breedOfTreeDescriptionJPA.getObjectByName(name);
    }

    @Override
    public List<BreedOfTreeDescription> findAll() {
        return breedOfTreeDescriptionJPA.findAll();
    }

    @Override
    public void deleteByID(int id) {
        breedOfTreeDescriptionJPA.deleteById(id);
    }
}
