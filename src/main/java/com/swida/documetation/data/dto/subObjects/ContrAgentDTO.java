package com.swida.documetation.data.dto.subObjects;

import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.ContrAgentType;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;

@Data
public class ContrAgentDTO {
    private int id;
    private String nameOfAgent;
    private String codeOfEDRPOY;
    private String country;
    private String addressOfCompany;
    private String phoneNumber1;
    private String phoneNumber2;
    private String email;
    private String contactFace;
    private String breedOfTree;

    private ContrAgentType contrAgentType;
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    public static ContrAgentDTO convertToDTO(ContrAgent agent){
        ContrAgentDTO dto = new ContrAgentDTO();
        dto.id = agent.getId();
        dto.nameOfAgent = agent.getNameOfAgent();
        dto.codeOfEDRPOY = agent.getCodeOfEDRPOY();
        dto.country = agent.getCountry();
        dto.addressOfCompany = agent.getAddressOfCompany();
        dto.phoneNumber1 = agent.getPhoneNumber1();
        dto.phoneNumber2 = agent.getPhoneNumber2();
        dto.email = agent.getEmail();
        dto.contactFace = agent.getContactFace();
        dto.breedOfTree = agent.getBreedOfTree();
        return dto;
    }
}
