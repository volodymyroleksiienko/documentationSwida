package com.swida.documetation.data.entity.subObjects;

import com.swida.documetation.data.enums.ContrAgentType;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ContrAgent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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



    @Enumerated(EnumType.STRING)
    private ContrAgentType contrAgentType;


    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
