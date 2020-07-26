package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class DryingStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfProduct;
    private String breedOfTree;

    private String sizeOfHeight;
    private String sizeOfWidth;
    private String sizeOfLong;

    private int countOfDesk;

    private String extent;
    private String description;

    @ManyToOne
    private UserCompany userCompany;

    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
