package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.dto.UserCompanyDTO;
import com.swida.documetation.data.dto.subObjects.BreedOfTreeDTO;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Data
public class RawStorageDTO {
    private int id;

    private String codeOfProduct;
    private BreedOfTreeDTO breedOfTree;
    private String breedDescription="";

    private String sizeOfHeight;
    private String sizeOfWidth="0";
    private String sizeOfLong="0";

    private int countOfDesk;
    private int maxCountOfDesk;

    private String extent="0.0";
    private String maxExtent="0.0";
    private String usedExtent="0.0";
    private String description;
    private String date;
    private Boolean getBuCutting = false;
    private QualityStatisticInfoDTO statisticInfo;
    private List<RawStorageDTO> groupedElements;
    private List<DescriptionDeskOakDTO> deskOakList;
    private UserCompanyDTO userCompany;
    private TreeStorageDTO treeStorage;
    private List<DryingStorageDTO> dryingStorageList;
    private TreeStorageDTO recycle;
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    @Override
    public String toString() {
        return "RawStorage{" +
                "id=" + id +
                ", codeOfProduct='" + codeOfProduct + '\'' +
                ", breedOfTree=" + breedOfTree +
                ", breedDescription='" + breedDescription + '\'' +
                ", sizeOfHeight='" + sizeOfHeight + '\'' +
                ", sizeOfWidth='" + sizeOfWidth + '\'' +
                ", sizeOfLong='" + sizeOfLong + '\'' +
                ", countOfDesk=" + countOfDesk +
                ", maxCountOfDesk=" + maxCountOfDesk +
                ", extent='" + extent + '\'' +
                ", maxExtent='" + maxExtent + '\'' +
                ", usedExtent='" + usedExtent + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
//                ", deskOakList=" + deskOakList +
                ", userCompany=" + userCompany +
//                ", treeStorage=" + treeStorage +
                ", dryingStorageList=" + dryingStorageList +
                ", recycle=" + recycle +
                ", statusOfEntity=" + statusOfEntity +
                '}';
    }
}
