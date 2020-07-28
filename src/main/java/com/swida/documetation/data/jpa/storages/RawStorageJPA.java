package com.swida.documetation.data.jpa.storages;

import com.swida.documetation.data.entity.storages.RawStorage;
import com.swida.documetation.data.entity.storages.TreeStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RawStorageJPA extends JpaRepository<RawStorage,Integer> {
    @Query("select r from  RawStorage  r where r.breedOfTree.id=?1 and  r.userCompany.id=?2")
    List<RawStorage> getListByUserByBreed(int breedId, int userId);
}
