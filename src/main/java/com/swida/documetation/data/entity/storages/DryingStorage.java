package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class DryingStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfProduct;
    @ManyToOne
    private BreedOfTree breedOfTree;
    private String breedDescription="";

    private String sizeOfHeight;
    private String sizeOfWidth="Не указано";
    private String sizeOfLong="Не указано";

    private int countOfDesk;

    private String extent;
    private String description;
    private String dateDrying;
    private String date;

    @OneToMany(mappedBy = "dryingStorage")
    private List<DescriptionDeskOak> deskOakList;

    @ManyToOne
    private UserCompany userCompany;
    @ManyToOne
    private RawStorage rawStorage;

    @OneToMany(mappedBy = "dryingStorage")
    private List<DryStorage> dryStorageList;

    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
