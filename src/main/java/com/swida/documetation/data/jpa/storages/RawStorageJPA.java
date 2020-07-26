package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.RawStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawStorageJPA extends JpaRepository<RawStorage,Integer> {
}
