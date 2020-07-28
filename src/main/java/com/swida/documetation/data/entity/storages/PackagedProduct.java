package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PackagedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfPackage;
    private String codeOfDeliveryCompany;
    //Detail of Product
    private String breedOfTree;
    private String quality;

    //Size of one desk
    private String sizeOfHeight;
    private String sizeOfWidth;
    private String sizeOfLong;

    //Info about package
    private String countDeskInHeight;
    private String countDeskInWidth;

    private String sumWidthOfPackage;
    private String sumHeightOfPackage;
    private String countOfBredInPackage;
    private String extentOfBredInPackage;

    @ManyToOne
    private UserCompany userCompany;

    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
