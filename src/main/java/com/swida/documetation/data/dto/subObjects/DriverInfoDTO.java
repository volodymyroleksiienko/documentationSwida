package com.swida.documetation.data.dto.subObjects;

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
}
