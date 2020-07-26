package com.swida.documetation.data.entity.subObjects;

import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class DriverInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String fullName;
    private String phone;
    private String numberOfTruck;
    private String numberOfTrailer;

    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
