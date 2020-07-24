package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.UserCompany;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class PackagedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String numberOfPackage;
    //Detail of Product
    private String accountLenght;
    private String quality;
    private String width;
    private String sizeOf;
    //Info about package
    private String sumWidthOfPackage;
    private String countOfBredInPackage;
    private String extentOfBredInPackage;

    @ManyToOne
    private UserCompany userCompany;



}
