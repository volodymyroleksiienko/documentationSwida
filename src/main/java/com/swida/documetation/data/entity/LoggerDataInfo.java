package com.swida.documetation.data.entity;

import com.swida.documetation.data.enums.LoggerOperationType;
import com.swida.documetation.data.enums.StorageType;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.util.Date;

@Entity
public class LoggerDataInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;

    @ManyToOne
    private UserCompany user;

    @Enumerated(EnumType.STRING)
    private LoggerOperationType operationType;
    @Enumerated(EnumType.STRING)
    private StorageType storageType;


}
