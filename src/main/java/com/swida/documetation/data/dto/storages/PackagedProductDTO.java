package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.dto.OrderInfoDTO;
import com.swida.documetation.data.dto.UserCompanyDTO;
import com.swida.documetation.data.dto.subObjects.ContainerDTO;
import com.swida.documetation.data.dto.subObjects.DeliveryDocumentationDTO;
import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.Container;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.StatusOfEntity;
import com.swida.documetation.data.enums.StatusOfProduct;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
public class PackagedProductDTO {
    private int id;

    private String codeOfPackage;
    private String codeOfDeliveryCompany;
    //Detail of Product
    private BreedOfTree breedOfTree;
    private String breedDescription="";
    private String quality;
    //Size of one desk
    private String sizeOfHeight;
    private String sizeOfWidth;
    private String sizeOfLong;
    //Info about package
    private String countDeskInHeight;
    private String countDeskInWidth;
    private String longFact;

    private String sumWidthOfPackage;
    private String sumHeightOfPackage;
    private String sumWidthOfAllDesk;
    private String countOfDesk;
    private String extent;
    private String height_width;

    private String date;

    private List<DescriptionDeskOakDTO> deskOakList;
    private OrderInfoDTO orderInfo;
    private ContainerDTO container;
    private UserCompanyDTO userCompany;
    private DryStorageDTO dryStorage;
    private DeliveryDocumentationDTO deliveryDocumentation;

    @Enumerated(EnumType.STRING)
    private StatusOfProduct statusOfProduct = StatusOfProduct.ON_STORAGE;

    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    @Override
    public String toString() {
        return "PackagedProduct{" +
                "id=" + id +
                ", codeOfPackage='" + codeOfPackage + '\'' +
                ", codeOfDeliveryCompany='" + codeOfDeliveryCompany + '\'' +
                ", breedOfTree=" + breedOfTree +
                ", breedDescription='" + breedDescription + '\'' +
                ", quality='" + quality + '\'' +
                ", sizeOfHeight='" + sizeOfHeight + '\'' +
                ", sizeOfWidth='" + sizeOfWidth + '\'' +
                ", sizeOfLong='" + sizeOfLong + '\'' +
                ", countDeskInHeight='" + countDeskInHeight + '\'' +
                ", countDeskInWidth='" + countDeskInWidth + '\'' +
                ", longFact='" + longFact + '\'' +
                ", sumWidthOfPackage='" + sumWidthOfPackage + '\'' +
                ", sumHeightOfPackage='" + sumHeightOfPackage + '\'' +
                ", sumWidthOfAllDesk='" + sumWidthOfAllDesk + '\'' +
                ", countOfDesk='" + countOfDesk + '\'' +
                ", extent='" + extent + '\'' +
                ", height_width='" + height_width + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
