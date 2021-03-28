package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.StatusOfEntity;
import com.swida.documetation.data.enums.StatusOfTreeStorage;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class TreeStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfProduct;
    private String breedDescription="";
    private String extent="0.0";
    private String maxExtent="0.0";
    private String date;

    private String averageDiameter;
    private String countOfDeck;

    private String treeDescription;


    @OneToMany(mappedBy = "treeStorage")
    private List<QualityStatisticInfo> statisticInfoList;

    @OneToMany
    private List<TreeStorage> recycle;

    @OneToMany(mappedBy = "treeStorage")
    private List<RawStorage> rawStorageList;

    @ManyToOne
    private BreedOfTree breedOfTree;
    @ManyToOne
    private OrderInfo orderInfo;
    @ManyToOne
    private ContrAgent contrAgent;
    @ManyToOne
    private UserCompany userCompany;
    @Enumerated(EnumType.STRING)
    private StatusOfTreeStorage statusOfTreeStorage = StatusOfTreeStorage.TREE;
    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    @Override
    public String toString() {
        return "TreeStorage{" +
                "id=" + id +
                ", codeOfProduct='" + codeOfProduct + '\'' +
                ", breedDescription='" + breedDescription + '\'' +
                ", extent='" + extent + '\'' +
                ", maxExtent='" + maxExtent + '\'' +
                ", date='" + date + '\'' +
                ", averageDiameter='" + averageDiameter + '\'' +
                ", countOfDeck='" + countOfDeck + '\'' +
                ", treeDescription='" + treeDescription + '\'' +
                ", breedOfTree=" + breedOfTree +
                ", orderInfo=" + orderInfo +
                ", contrAgent=" + contrAgent +
                ", userCompany=" + userCompany +
                ", statusOfTreeStorage=" + statusOfTreeStorage +
                ", statusOfEntity=" + statusOfEntity +
                '}';
    }
}
