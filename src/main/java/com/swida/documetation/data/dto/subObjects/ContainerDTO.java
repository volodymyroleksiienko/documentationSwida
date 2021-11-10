package com.swida.documetation.data.dto.subObjects;

import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;

@Data
public class ContainerDTO {
    private int id;

    private String code;
    private String codeOfOrderInfo;
    private String extent;
    private String costOfUploading;
    private String costOfDeliveryToPort= "80";
    private String costOfWeighing = "100";
    private String coefUploading = "4.00";
    private String exchangeRate = "0.00";
    private String equalsToUAH;

    private String date;

    private ContrAgentDTO contrAgent;

    private StatusOfEntity statusOfEntity=StatusOfEntity.ACTIVE;

    private DeliveryDestinationType destinationType = DeliveryDestinationType.MULTIMODAL;
}
