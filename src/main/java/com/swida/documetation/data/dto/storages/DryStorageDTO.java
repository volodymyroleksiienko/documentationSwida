package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
public class DryStorageDTO {

    private int id;

    private String codeOfProduct;

    private BreedOfTree breedOfTree;
    private String breedDescription="";

    private String sizeOfHeight;
    private String sizeOfWidth="0";
    private String sizeOfLong="0";

    private int countOfDesk;

    private String extent;
    private String maxExtent;
    private String description;
    private String date;

    private String qualityOfPack;
    private String longOfPack;

    private Boolean wasWithDeskOakList;


    private List<DryStorageDTO> groupedElements;


    private List<DescriptionDeskOakDTO> deskOakList;


    private List<PackagedProductDTO> packagedProductList;

    private UserCompany userCompany;

    private DryingStorageDTO dryingStorage;
    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
