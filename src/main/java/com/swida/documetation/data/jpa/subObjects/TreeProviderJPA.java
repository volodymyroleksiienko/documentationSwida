package com.swida.documetation.data.jpa.subObjects;

import com.swida.documetation.data.entity.subObjects.TreeProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TreeProviderJPA extends JpaRepository<TreeProvider,Integer> {
    @Query("select count(pr.id) from TreeProvider pr where pr.nameOfTreeProvider=?1")
    int countExistObj(String nameOfTreeProvider);

    @Query("select pr.id from TreeProvider pr where pr.nameOfTreeProvider=?1")
    int getIdByUsername(String nameOfTreeProvider);
}
