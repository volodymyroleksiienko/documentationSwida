package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Data
public class DryingStorageDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfProduct;
    private BreedOfTree breedOfTree;
    private String breedDescription="";

    private String sizeOfHeight;
    private String sizeOfWidth="0";
    private String sizeOfLong="0";
    private Integer cell;

    private int countOfDesk;

    private String extent;
    private String description;
    private String dateDrying;
    private String startDate;
    private String date;
    private List<DescriptionDeskOakDTO> deskOakList;

    private UserCompany userCompany;
    private RawStorageDTO rawStorage;

    private List<DryStorageDTO> dryStorageList;

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
