package com.swida.documetation.data.jpa.subObjects;

import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BreedOfTreeJPA extends JpaRepository<BreedOfTree,Integer> {
    @Query("select obj from  BreedOfTree obj where obj.breed=?1")
    BreedOfTree getObjectByName (String name);
}
