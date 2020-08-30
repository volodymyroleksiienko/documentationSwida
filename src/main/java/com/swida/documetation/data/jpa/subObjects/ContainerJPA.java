package com.swida.documetation.data.jpa.subObjects;

import com.swida.documetation.data.entity.subObjects.Container;
import com.swida.documetation.data.enums.StatusOfEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContainerJPA extends JpaRepository<Container, Integer > {
    @Query("select obj from Container obj where  obj.statusOfEntity=?1")
    List<Container> selectByStatusOfEntity(StatusOfEntity status);
}
