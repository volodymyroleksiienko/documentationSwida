package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.DryStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DryStorageJPA extends JpaRepository<DryStorage,Integer> {
}
