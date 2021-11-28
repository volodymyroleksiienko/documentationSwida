package com.swida.documetation.data.dto.subObjects;

import com.swida.documetation.data.entity.subObjects.DriverInfo;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;

@Data
public class DriverInfoDTO {
    private int id;

    private String fullName;
    //for multimodal doc
    private String idOfTruck;
    private String phone;
    private String numberOfTruck;
    private String numberOfTrailer;
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    public static DriverInfoDTO convertToDTO(DriverInfo info){
        DriverInfoDTO dto = new DriverInfoDTO();
        dto.id = info.getId();
        dto.fullName = info.getFullName();
        dto.idOfTruck = info.getIdOfTruck();
        dto.phone = info.getPhone();
        dto.numberOfTruck = info.getNumberOfTruck();
        dto.numberOfTrailer = info.getNumberOfTrailer();
        dto.statusOfEntity = info.getStatusOfEntity();

        return dto;
    }
}
