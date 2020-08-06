package com.swida.documetation.data.jpa.subObjects;

import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeliveryDocumentationJPA extends JpaRepository<DeliveryDocumentation,Integer> {
    @Query("select t from  DeliveryDocumentation  t where t.breedOfTree.id=?1 and  t.userCompany.id=?2 ")
    List<DeliveryDocumentation> getListByUserByBreed(int breedId, int userId);
}
