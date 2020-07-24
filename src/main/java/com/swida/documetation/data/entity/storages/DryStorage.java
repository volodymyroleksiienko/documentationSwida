package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.UserCompany;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class DryStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfProduct;
    private String breedOfTree;
    private String sizeOf;
    private String extent;
    private String description;
    // For detection product in process or on storage
    private String status;
    private String dateOfFinish;

    @ManyToOne
    private UserCompany userCompany;
}
