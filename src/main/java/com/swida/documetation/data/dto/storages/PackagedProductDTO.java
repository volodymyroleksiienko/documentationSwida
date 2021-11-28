package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.dto.OrderInfoDTO;
import com.swida.documetation.data.dto.UserCompanyDTO;
import com.swida.documetation.data.dto.subObjects.BreedOfTreeDTO;
import com.swida.documetation.data.dto.subObjects.ContainerDTO;
import com.swida.documetation.data.dto.subObjects.DeliveryDocumentationDTO;
import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.Container;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.StatusOfEntity;
import com.swida.documetation.data.enums.StatusOfProduct;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class PackagedProductDTO {
    private int id;

    private String codeOfPackage;
    private String codeOfDeliveryCompany;
    //Detail of Product
    private BreedOfTreeDTO breedOfTree;
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


    private StatusOfProduct statusOfProduct = StatusOfProduct.ON_STORAGE;
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    public static PackagedProductDTO convertToDTO(PackagedProduct product){
        PackagedProductDTO dto = new PackagedProductDTO();
        dto.id = product.getId();
        dto.codeOfPackage = product.getCodeOfPackage();
        dto.codeOfDeliveryCompany = product.getCodeOfDeliveryCompany();
        if(product.getBreedOfTree()!=null) {
            dto.breedOfTree = BreedOfTreeDTO.convertToDTO(product.getBreedOfTree());
        }
        dto.breedDescription = product.getBreedDescription();
        dto.quality = product.getQuality();

        dto.sizeOfHeight = product.getSizeOfHeight();
        dto.sizeOfWidth = product.getSizeOfWidth();
        dto.sizeOfLong = product.getSizeOfLong();

        dto.countDeskInHeight = product.getCountDeskInHeight();
        dto.countDeskInWidth = product.getCountDeskInWidth();
        dto.longFact = product.getLongFact();

        dto.sumWidthOfPackage = product.getSumWidthOfPackage();
        dto.sumHeightOfPackage = product.getSumHeightOfPackage();
        dto.sumWidthOfAllDesk = product.getSumWidthOfAllDesk();
        dto.countOfDesk = product.getCountOfDesk();
        dto.extent = product.getExtent();
        dto.height_width = product.getHeight_width();

        dto.date = product.getDate();

        if(product.getDeskOakList()!=null && product.getDeskOakList().size()>0) {
            dto.deskOakList = DescriptionDeskOakDTO.convertToDTO(product.getDeskOakList());
        }
        if(product.getOrderInfo()!=null) {
            dto.orderInfo = OrderInfoDTO.convertToDTO(product.getOrderInfo());
        }
        if(product.getContainer()!=null){
            dto.container = ContainerDTO.convertToDTO(product.getContainer());
        }
        if(product.getUserCompany()!=null){
            dto.userCompany = UserCompanyDTO.convertToDTO(product.getUserCompany());
        }
        if(product.getDryStorage()!=null){
            dto.dryStorage = DryStorageDTO.convertToDTO(product.getDryStorage());
        }
        if(product.getDeliveryDocumentation()!=null){
            dto.deliveryDocumentation = DeliveryDocumentationDTO.convertToDTO(product.getDeliveryDocumentation(),false);
        }
        dto.statusOfProduct = product.getStatusOfProduct();
        dto.statusOfEntity = product.getStatusOfEntity();
        return dto;
    }

    public static List<PackagedProductDTO> convertToDTO(List<PackagedProduct> list){
        List<PackagedProductDTO> dtoList = new ArrayList<>();
        for(PackagedProduct item: list){
            dtoList.add(convertToDTO(item));
        }
        return dtoList;
    }


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
