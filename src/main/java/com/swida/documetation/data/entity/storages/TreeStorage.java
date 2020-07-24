package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.UserCompany;
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
    private String provider;
    private String extent;

    @ManyToOne
    private UserCompany userCompany;
}
