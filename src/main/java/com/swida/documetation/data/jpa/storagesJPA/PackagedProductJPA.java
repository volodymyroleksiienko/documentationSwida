package com.swida.documetation.data.jpa.storagesJPA;

import com.swida.documetation.data.entity.storages.PackagedProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackagedProductJPA extends JpaRepository<PackagedProduct,Integer> {
}
