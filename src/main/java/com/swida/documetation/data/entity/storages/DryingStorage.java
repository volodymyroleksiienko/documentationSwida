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
    private String breedDescription = "";

    private String sizeOfHeight;
    private String sizeOfWidth = "0";
    private String sizeOfLong = "0";
    private Integer cell;

    private int countOfDesk;

    private String extent;
    private String description;
    private String dateDrying;
    private String startDate;
    private String date;

    @OneToMany(mappedBy = "dryingStorage", fetch = FetchType.LAZY)
    private List<DescriptionDeskOak> deskOakList;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserCompany userCompany;
    @ManyToOne(fetch = FetchType.LAZY)
    private RawStorage rawStorage;

    @OneToMany(mappedBy = "dryingStorage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DryStorage> dryStorageList;

    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    @Override
    public String toString() {
        return "DryingStorage{" +
                "id=" + id +
                ", codeOfProduct='" + codeOfProduct + '\'' +
                ", breedOfTree=" + breedOfTree +
                ", breedDescription='" + breedDescription + '\'' +
                ", sizeOfHeight='" + sizeOfHeight + '\'' +
                ", sizeOfWidth='" + sizeOfWidth + '\'' +
                ", sizeOfLong='" + sizeOfLong + '\'' +
                ", cell=" + cell +
                ", countOfDesk=" + countOfDesk +
                ", extent='" + extent + '\'' +
                ", description='" + description + '\'' +
                ", dateDrying='" + dateDrying + '\'' +
                ", startDate='" + startDate + '\'' +
                ", date='" + date + '\'' +
                ", statusOfEntity=" + statusOfEntity +
                '}';
    }
}
