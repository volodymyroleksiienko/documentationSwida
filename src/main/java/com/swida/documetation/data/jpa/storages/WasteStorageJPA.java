package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.WasteStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WasteStorageJPA extends JpaRepository<WasteStorage,Integer> {
}
