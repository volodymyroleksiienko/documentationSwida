package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PackagedProductJPA extends JpaRepository<PackagedProduct,Integer> {
    @Query("select r from  PackagedProduct  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2")
    List<PackagedProduct> getListByUserByBreed(int breedId, int userId);
}
