package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.TreeStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreeStorageJPA extends JpaRepository<TreeStorage,Integer> {
}
