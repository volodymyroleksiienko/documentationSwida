package com.swida.documetation.data.entity;

import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfEntity;
import com.swida.documetation.data.enums.StatusOfOrderInfo;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String codeOfOrder;
    private String extentOfOrder="0.000";
    private String doneExtendOfOrder="0.000";
    private String toDoExtentOfOrder="0.000";
    private String extentForDistribution="0.000";
    private String extentInContainer="0.000";
    private String extentWithoutContainer="0.000";
    private String date;


    @ManyToOne(fetch = FetchType.LAZY)
    private OrderInfo mainOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    private BreedOfTree breedOfTree;
    private String breedDescription="";
    @OneToOne(fetch = FetchType.LAZY)
    private ContrAgent contrAgent;
    @Enumerated(EnumType.STRING)
    private DeliveryDestinationType destinationType;
    @Enumerated(EnumType.STRING)
    private StatusOfOrderInfo statusOfOrderInfo;
    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
