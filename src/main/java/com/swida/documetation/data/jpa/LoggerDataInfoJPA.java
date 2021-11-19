package com.swida.documetation.data.jpa;

import com.swida.documetation.data.entity.LoggerDataInfo;
import com.swida.documetation.data.enums.LoggerOperationType;
import com.swida.documetation.data.enums.StorageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface LoggerDataInfoJPA extends JpaRepository<LoggerDataInfo,Integer> {

    @Query("select ld from LoggerDataInfo ld " +
            " where (ld.date between ?1 and ?2)" +
            " and (ld.breedOfTree.id in ?3 or ?3 is null)" +
            " and (ld.user.id in ?4 or ?4 is null)" +
            " and (ld.storageType in ?5 or ?5 is null)" +
            " and (ld.operationType in ?6 or ?6 is null)"+
            " order by ld.date DESC "
    )
    List<LoggerDataInfo> findFiltered(Date dateFrom, Date dateTo, List<Integer> breedId, List<Integer> users,
                                      List<StorageType> storageType, List<LoggerOperationType> actions);
}
