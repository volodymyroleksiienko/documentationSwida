package com.swida.documetation.data.entity.subObjects;

import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Container {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;
    private String extent;
    private String costOfUploading;
    private String costOfDeliveryToPort;
    private String costOfWeighing;
    private String exchangeRate;
    private String equalsToUAH;

    private String date;

    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity=StatusOfEntity.ACTIVE;

}
