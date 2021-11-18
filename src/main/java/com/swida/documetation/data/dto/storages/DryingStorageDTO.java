package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.dto.UserCompanyDTO;
import com.swida.documetation.data.dto.subObjects.BreedOfTreeDTO;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.DryingStorage;
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
    private BreedOfTreeDTO breedOfTree;
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

    private UserCompanyDTO userCompany;
    private RawStorageDTO rawStorage;

//    private List<DryStorageDTO> dryStorageList;

    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    public static DryingStorageDTO convertToDTO(DryingStorage storage){
        DryingStorageDTO dto = new DryingStorageDTO();
        dto.id = storage.getId();
        dto.codeOfProduct = storage.getCodeOfProduct();
        dto.breedOfTree = BreedOfTreeDTO.convertToDTO(storage.getBreedOfTree());
        dto.breedDescription = storage.getBreedDescription();
        dto.sizeOfHeight = storage.getSizeOfHeight();
        dto.sizeOfWidth = storage.getSizeOfWidth();
        dto.sizeOfLong = storage.getSizeOfLong();
        dto.cell = storage.getCell();
        dto.countOfDesk = storage.getCountOfDesk();
        dto.extent = storage.getExtent();
        dto.description = storage.getDescription();
        dto.dateDrying = storage.getDateDrying();
        dto.startDate = storage.getStartDate();
        dto.date = storage.getDate();
        if(storage.getDeskOakList()!=null && storage.getDeskOakList().size()>0) {
            dto.deskOakList = DescriptionDeskOakDTO.convertToDTO(storage.getDeskOakList());
        }
        if(storage.getUserCompany()!=null) {
            dto.userCompany = UserCompanyDTO.convertToDTO(storage.getUserCompany());
        }
        if(storage.getRawStorage()!=null) {
            dto.rawStorage = RawStorageDTO.convertToDTO(storage.getRawStorage());
        }
        dto.statusOfEntity = storage.getStatusOfEntity();
        return dto;
    }

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
