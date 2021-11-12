package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.dto.subObjects.DeliveryDocumentationDTO;
import com.swida.documetation.data.entity.storages.DescriptionDeskOak;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class DescriptionDeskOakDTO {
    private int id;
    private String sizeOfWidth;
    private String countOfDesk;

//    private DryingStorageDTO dryingStorage;
//    private DryStorageDTO dryStorage;
//    private RawStorageDTO rawStorage;
//    private PackagedProductDTO packagedProduct;
//    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    public static DescriptionDeskOakDTO convertToDTO(DescriptionDeskOak oak){
        DescriptionDeskOakDTO dto = new DescriptionDeskOakDTO();
        dto.id = oak.getId();
        dto.sizeOfWidth = oak.getSizeOfWidth();
        dto.countOfDesk = oak.getCountOfDesk();

        return dto;
    }

    public static List<DescriptionDeskOakDTO> convertToDTO(List<DescriptionDeskOak> oak){
        List<DescriptionDeskOakDTO> list =  new ArrayList<>();
        for(DescriptionDeskOak item:oak){
            list.add(convertToDTO(item));
        }
        return list;
    }

}
