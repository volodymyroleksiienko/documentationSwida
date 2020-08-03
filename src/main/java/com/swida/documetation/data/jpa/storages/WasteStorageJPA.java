package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.WasteStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WasteStorageJPA extends JpaRepository<WasteStorage,Integer> {
    @Query("select r from  WasteStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2")
    List<WasteStorage> getListByUserByBreed(int breedId, int userId);
}
