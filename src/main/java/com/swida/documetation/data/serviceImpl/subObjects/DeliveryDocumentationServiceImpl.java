package com.swida.documetation.data.serviceImpl.subObjects;

import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.jpa.subObjects.DeliveryDocumentationJPA;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DeliveryDocumentationServiceImpl implements DeliveryDocumentationService {
    DeliveryDocumentationJPA documentationJPA;

    @Autowired
    public DeliveryDocumentationServiceImpl(DeliveryDocumentationJPA documentationJPA) {
        this.documentationJPA = documentationJPA;
    }

    @Override
    public void save(DeliveryDocumentation doc) {
        documentationJPA.save(doc);
    }

    @Override
    public DeliveryDocumentation findById(int id) {
        return documentationJPA.getOne(id);
    }

    @Override
    public List<DeliveryDocumentation> findAll() {
        return documentationJPA.findAll();
    }

    @Override
    public List<DeliveryDocumentation> getListByUserByBreed(int breedId,int userId) {
        return documentationJPA.getListByUserByBreed(breedId,userId);
    }

    @Override
    public void deleteByID(int id) {
        documentationJPA.deleteById(id);
    }
}
