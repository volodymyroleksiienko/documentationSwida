package com.swida.documetation.data.entity.subObjects;

import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class DriverInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fullName;
    //for multimodal doc
    private String idOfTruck;
    private String phone;
    private String numberOfTruck;
    private String numberOfTrailer;
    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
