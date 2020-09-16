package com.swida.documetation.data.entity.subObjects;

import com.swida.documetation.data.enums.DeliveryDestinationType;
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
    private String codeOfOrderInfo;
    private String extent;
    private String costOfUploading;
    private String costOfDeliveryToPort= "80";
    private String costOfWeighing = "100";
    private String coefUploading = "4.00";
    private String exchangeRate = "0.00";
    private String equalsToUAH;

    private String date;

    @ManyToOne
    private ContrAgent contrAgent;

    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity=StatusOfEntity.ACTIVE;

    @Enumerated(EnumType.STRING)
    private DeliveryDestinationType destinationType = DeliveryDestinationType.MULTIMODAL;
}
