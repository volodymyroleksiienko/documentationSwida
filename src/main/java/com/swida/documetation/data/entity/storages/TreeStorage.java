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
    private String extent;
    private String maxExtent;
    private String date;

    private String averageDiameter;
    private String countOfDeck;


    @OneToMany
    private List<QualityStatisticInfo> statisticInfoList;

    @OneToOne
    private TreeStorage recycle;

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
}
