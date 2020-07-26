package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.TreeProvider;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class TreeStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfProduct;
    private String breedOfTree;
    private String extent;

    @ManyToOne
    private TreeProvider treeProvider;
    @ManyToOne
    private UserCompany userCompany;

    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
