package com.swida.documetation.data.entity;

import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.LoggerOperationType;
import com.swida.documetation.data.enums.StorageType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class LoggerDataInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;

    @ManyToOne
    private BreedOfTree breedOfTree;

    @ManyToOne
    private UserCompany user;

    @Enumerated(EnumType.STRING)
    private LoggerOperationType operationType;
    @Enumerated(EnumType.STRING)
    private StorageType storageType;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String objectBeforeChanging;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String objectAfterChanging;

    public LoggerDataInfo() {
    }

    public LoggerDataInfo(BreedOfTree breedOfTree, UserCompany user, LoggerOperationType operationType,
                          StorageType storageType, String objectBeforeChanging, String objectAfterChanging) {
        this.date = new Date();
        this.breedOfTree = breedOfTree;
        this.user = user;
        this.operationType = operationType;
        this.storageType = storageType;
        this.objectBeforeChanging = objectBeforeChanging;
        this.objectAfterChanging = objectAfterChanging;
    }
}
