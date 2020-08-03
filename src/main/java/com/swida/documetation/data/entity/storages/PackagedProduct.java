package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.StatusOfEntity;
import com.swida.documetation.data.enums.StatusOfProduct;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class PackagedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfPackage;
    private String codeOfDeliveryCompany;
    //Detail of Product
    @ManyToOne
    private BreedOfTree breedOfTree;
    private String breedDescription;
    private String quality;

    //Size of one desk
    private String sizeOfHeight;
    private String sizeOfWidth;
    private String sizeOfLong;


    //Info about package
    private String countDeskInHeight;
    private String countDeskInWidth;
    private String longFact;

    private String sumWidthOfPackage;
    private String sumHeightOfPackage;
    private String sumWidthOfAllDesk;
    private String countOfDesk;
    private String extent;
    private String height_width;

    private String date;

    @OneToMany
    private List<DescriptionDeskOak> deskOakList;

    @ManyToOne
    private UserCompany userCompany;

    @ManyToOne
    private DryStorage dryStorage;

    @Enumerated(EnumType.STRING)
    private StatusOfProduct statusOfProduct = StatusOfProduct.ON_STORAGE;

    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
