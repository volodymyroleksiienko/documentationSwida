package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.dto.UserCompanyDTO;
import com.swida.documetation.data.dto.subObjects.BreedOfTreeDTO;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.DryStorage;
import com.swida.documetation.data.entity.storages.DryingStorage;
import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class DryStorageDTO {

    private int id;

    private String codeOfProduct;

    private BreedOfTreeDTO breedOfTree;
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
//    private List<PackagedProductDTO> packagedProductList;
    private UserCompanyDTO userCompany;

    private DryingStorageDTO dryingStorage;
    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    public static DryStorageDTO convertToDTO(DryStorage storage){
        DryStorageDTO dto = new DryStorageDTO();
        dto.id = storage.getId();
        dto.codeOfProduct = storage.getCodeOfProduct();
        dto.breedOfTree = BreedOfTreeDTO.convertToDTO(storage.getBreedOfTree());
        dto.breedDescription = storage.getBreedDescription();
        dto.sizeOfHeight = storage.getSizeOfHeight();
        dto.sizeOfWidth = storage.getSizeOfWidth();
        dto.sizeOfLong = storage.getSizeOfLong();
        dto.countOfDesk = storage.getCountOfDesk();
        dto.extent = storage.getExtent();
        dto.maxExtent = storage.getMaxExtent();
        dto.description = storage.getDescription();
        dto.date = storage.getDate();
        dto.qualityOfPack = storage.getDate();
        dto.longOfPack = storage.getDate();
        dto.wasWithDeskOakList = storage.getWasWithDeskOakList();
        if(storage.getGroupedElements()!=null && storage.getGroupedElements().size()>0){
            dto.groupedElements = convertToDTO(storage.getGroupedElements());
        }
        if(storage.getDeskOakList()!=null && storage.getDeskOakList().size()>0) {
            dto.deskOakList = DescriptionDeskOakDTO.convertToDTO(storage.getDeskOakList());
        }
        if(storage.getUserCompany()!=null) {
            dto.userCompany = UserCompanyDTO.convertToDTO(storage.getUserCompany());
        }
        if(storage.getDryingStorage()!=null) {
            dto.dryingStorage = DryingStorageDTO.convertToDTO(storage.getDryingStorage());
        }
        dto.statusOfEntity = storage.getStatusOfEntity();
        return dto;
    }

    public static List<DryStorageDTO> convertToDTO(List<DryStorage> info){
        List<DryStorageDTO> dto = new ArrayList<>();
        for(DryStorage item : info){
            dto.add(DryStorageDTO.convertToDTO(item));
        }
        return dto;
    }
}
