package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class DryStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfProduct;
    @ManyToOne
    private BreedOfTree breedOfTree;
    private String breedDescription = "";

    private String sizeOfHeight;
    private String sizeOfWidth = "0";
    private String sizeOfLong = "0";

    private int countOfDesk;

    private String extent;
    private String maxExtent;
    private String description;
    private String date;

    private String qualityOfPack;
    private String longOfPack;

    private Boolean wasWithDeskOakList;

    @OneToMany(fetch = FetchType.LAZY)
    private List<DryStorage> groupedElements;

    @OneToMany(mappedBy = "dryStorage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DescriptionDeskOak> deskOakList;

    @OneToMany(mappedBy = "dryStorage", fetch = FetchType.LAZY)
    private List<PackagedProduct> packagedProductList;
    @ManyToOne(fetch = FetchType.LAZY)
    private UserCompany userCompany;
    @ManyToOne(fetch = FetchType.LAZY)
    private DryingStorage dryingStorage;
    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
