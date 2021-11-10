package com.swida.documetation.data.dto;

import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfEntity;
import com.swida.documetation.data.enums.StatusOfOrderInfo;
import lombok.Data;

import javax.persistence.*;


@Data
public class OrderInfoDTO {
    private int id;
    private String codeOfOrder;
    private String extentOfOrder="0.000";
    private String doneExtendOfOrder="0.000";
    private String toDoExtentOfOrder="0.000";
    private String extentForDistribution="0.000";
    private String extentInContainer="0.000";
    private String extentWithoutContainer="0.000";
    private String date;

    private OrderInfoDTO mainOrder;
    private BreedOfTree breedOfTree;
    private String breedDescription="";

    private ContrAgent contrAgent;
    private DeliveryDestinationType destinationType;
    private StatusOfOrderInfo statusOfOrderInfo;
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
