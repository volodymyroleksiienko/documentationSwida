package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.dto.UserCompanyDTO;
import com.swida.documetation.data.dto.subObjects.BreedOfTreeDTO;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.RawStorage;
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
//    private QualityStatisticInfoDTO statisticInfo;
//    private List<RawStorageDTO> groupedElements;
//    private List<DescriptionDeskOakDTO> deskOakList;
    private UserCompanyDTO userCompany;
    private TreeStorageDTO treeStorage;
//    private List<DryingStorageDTO> dryingStorageList;
//    private TreeStorageDTO recycle;
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    public static RawStorageDTO convertToDTO(RawStorage rawStorage){
        RawStorageDTO dto = new RawStorageDTO();
        dto.id = rawStorage.getId();
        dto.codeOfProduct = rawStorage.getCodeOfProduct();
        dto.breedOfTree = BreedOfTreeDTO.convertToDTO(rawStorage.getBreedOfTree());
        dto.breedDescription = rawStorage.getBreedDescription();
        dto.sizeOfHeight = rawStorage.getSizeOfHeight();
        dto.sizeOfWidth = rawStorage.getSizeOfWidth();
        dto.sizeOfLong = rawStorage.getSizeOfLong();
        dto.countOfDesk = rawStorage.getCountOfDesk();
        dto.maxCountOfDesk = rawStorage.getMaxCountOfDesk();
        dto.extent = rawStorage.getExtent();
        dto.maxExtent = rawStorage.getMaxExtent();
        dto.usedExtent = rawStorage.getUsedExtent();
        dto.description = rawStorage.getDescription();
        dto.date = rawStorage.getDate();
        dto.getBuCutting = rawStorage.getGetBuCutting();
        dto.userCompany = UserCompanyDTO.convertToDTO(rawStorage.getUserCompany());
        dto.treeStorage = TreeStorageDTO.convertToDTO(rawStorage.getTreeStorage());
        dto.statusOfEntity = rawStorage.getStatusOfEntity();
        return dto;
    }

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
                ", treeStorage=" + treeStorage +
//                ", dryingStorageList=" + dryingStorageList +
//                ", recycle=" + recycle +
                ", statusOfEntity=" + statusOfEntity +
                '}';
    }
}
