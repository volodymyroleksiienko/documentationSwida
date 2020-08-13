package com.swida.documetation.data.jpa.subObjects;

import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.BreedOfTreeDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BreedOfTreeDescriptionJPA extends JpaRepository<BreedOfTreeDescription,Integer> {
    @Query("select obj from  BreedOfTreeDescription obj where obj.description=?1")
    BreedOfTreeDescription getObjectByName (String name);
}
