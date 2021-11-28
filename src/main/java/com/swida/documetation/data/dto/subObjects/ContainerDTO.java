package com.swida.documetation.data.dto.subObjects;

import com.swida.documetation.data.entity.subObjects.Container;
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

    public static ContainerDTO convertToDTO(Container container){
        ContainerDTO dto = new ContainerDTO();
        dto.id = container.getId();
        dto.code = container.getCode();
        dto.codeOfOrderInfo = container.getCodeOfOrderInfo();
        dto.extent = container.getExtent();
        dto.costOfUploading = container.getCostOfUploading();
        dto.costOfDeliveryToPort = container.getCostOfDeliveryToPort();
        dto.costOfWeighing = container.getCostOfWeighing();
        dto.coefUploading = container.getCoefUploading();
        dto.exchangeRate = container.getExchangeRate();
        dto.equalsToUAH = container.getEqualsToUAH();
        dto.date = container.getDate();

        if(container.getContrAgent()!=null){
            dto.contrAgent = ContrAgentDTO.convertToDTO(container.getContrAgent());
        }
        dto.statusOfEntity = container.getStatusOfEntity();
        dto.destinationType = container.getDestinationType();
        return dto;
    }
}
