package com.swida.documetation.data.jpa.subObjects;

import com.swida.documetation.data.entity.subObjects.ContrAgent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContrAgentJPA extends JpaRepository<ContrAgent,Integer> {
    @Query("select count(pr.id) from ContrAgent pr where pr.nameOfAgent=?1")
    int countExistObj(String nameOfTreeProvider);

    @Query("select pr.id from ContrAgent pr where pr.nameOfAgent=?1")
    int getIdByUsername(String nameOfTreeProvider);
}
