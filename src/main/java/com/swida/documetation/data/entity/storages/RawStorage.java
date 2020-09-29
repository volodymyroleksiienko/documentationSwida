package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class RawStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfProduct;
    @ManyToOne
    private BreedOfTree breedOfTree;
    private String breedDescription="";

    private String sizeOfHeight;
    private String sizeOfWidth;
    private String sizeOfLong;

    private int countOfDesk;
    private int maxCountOfDesk;

    private String extent;
    private String description;
    private String date;

    @ManyToOne
    private UserCompany userCompany;

    @ManyToOne
    private TreeStorage treeStorage;
    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
