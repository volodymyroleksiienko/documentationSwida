package com.swida.documetation.data.dto;

import com.swida.documetation.data.dto.subObjects.BreedOfTreeDTO;
import com.swida.documetation.data.dto.subObjects.ContrAgentDTO;
import com.swida.documetation.data.entity.OrderInfo;
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
    private BreedOfTreeDTO breedOfTree;
    private String breedDescription="";

    private ContrAgentDTO contrAgent;
    private DeliveryDestinationType destinationType;
    private StatusOfOrderInfo statusOfOrderInfo;
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    public static OrderInfoDTO convertToDTO(OrderInfo info){
        OrderInfoDTO  dto = new OrderInfoDTO();
        dto.id = info.getId();
        dto.codeOfOrder = info.getCodeOfOrder();
        dto.extentOfOrder = info.getExtentOfOrder();
        dto.doneExtendOfOrder = info.getDoneExtendOfOrder();
        dto.toDoExtentOfOrder = info.getToDoExtentOfOrder();
        dto.extentForDistribution = info.getExtentForDistribution();
        dto.extentInContainer = info.getExtentInContainer();
        dto.extentWithoutContainer = info.getExtentWithoutContainer();
        dto.date = info.getDate();

        if(info.getMainOrder()!=null){
            dto.mainOrder = convertToDTO(info.getMainOrder());
        }
        if(info.getBreedOfTree()!=null){
            dto.breedOfTree = BreedOfTreeDTO.convertToDTO(info.getBreedOfTree());
        }
        dto.breedDescription = info.getBreedDescription();
        if(info.getContrAgent()!=null){
            dto.contrAgent = ContrAgentDTO.convertToDTO(info.getContrAgent());
        }
        dto.destinationType = info.getDestinationType();
        dto.statusOfOrderInfo = info.getStatusOfOrderInfo();
        dto.statusOfEntity = info.getStatusOfEntity();
        return dto;
    }
}
