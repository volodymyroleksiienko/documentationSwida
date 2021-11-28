package com.swida.documetation.data.dto.subObjects;

import com.swida.documetation.data.dto.OrderInfoDTO;
import com.swida.documetation.data.dto.UserCompanyDTO;
import com.swida.documetation.data.dto.storages.PackagedProductDTO;
import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Data
public class DeliveryDocumentationDTO {
    private int id;

    private String dateOfUnloading;
    private String timeOfUnloading;
    private String clientName;
    private String description;
    private String packagesExtent;
    private String sizeOfHeightList;
    private String extentWithoutContainer="0.000";

    private OrderInfoDTO orderInfo;
    private BreedOfTreeDTO breedOfTree;
    private DriverInfoDTO driverInfo;
    private ContrAgentDTO contrAgent;
    private UserCompanyDTO userCompany;
    private List<PackagedProductDTO> productList;
    private DeliveryDestinationType destinationType;
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    public static DeliveryDocumentationDTO convertToDTO(DeliveryDocumentation documentation, Boolean includeProductList){
        DeliveryDocumentationDTO dto = new DeliveryDocumentationDTO();
        dto.id = documentation.getId();
        dto.dateOfUnloading = documentation.getDateOfUnloading();
        dto.timeOfUnloading = documentation.getTimeOfUnloading();
        dto.clientName = documentation.getClientName();
        dto.description = documentation.getDescription();
        dto.packagesExtent = documentation.getPackagesExtent();
        dto.sizeOfHeightList = documentation.getSizeOfHeightList();
        dto.extentWithoutContainer = documentation.getExtentWithoutContainer();
        if(documentation.getOrderInfo()!=null) {
            dto.orderInfo = OrderInfoDTO.convertToDTO(documentation.getOrderInfo());
        }
        if(documentation.getBreedOfTree()!=null) {
            dto.breedOfTree = BreedOfTreeDTO.convertToDTO(documentation.getBreedOfTree());
        }
        if(documentation.getDriverInfo()!=null) {
            dto.driverInfo = DriverInfoDTO.convertToDTO(documentation.getDriverInfo());
        }
        if(documentation.getContrAgent()!=null) {
            dto.contrAgent = ContrAgentDTO.convertToDTO(documentation.getContrAgent());
        }
        if(documentation.getUserCompany()!=null) {
            dto.userCompany = UserCompanyDTO.convertToDTO(documentation.getUserCompany());
        }
        if(documentation.getProductList()!=null && documentation.getProductList().size()>0 && includeProductList) {
            dto.productList = PackagedProductDTO.convertToDTO(documentation.getProductList());
        }
        dto.destinationType = documentation.getDestinationType();
        dto.statusOfEntity = documentation.getStatusOfEntity();
        return dto;
    }

    @Override
    public String toString() {
        return "DeliveryDocumentation{" +
                "id=" + id +
                ", dateOfUnloading='" + dateOfUnloading + '\'' +
                ", timeOfUnloading='" + timeOfUnloading + '\'' +
                ", clientName='" + clientName + '\'' +
                ", description='" + description + '\'' +
                ", packagesExtent='" + packagesExtent + '\'' +
                ", orderInfo=" + orderInfo +
                ", breedOfTree=" + breedOfTree +
                ", driverInfo=" + driverInfo +
                ", contrAgent=" + contrAgent +
                ", userCompany=" + userCompany +
                ", destinationType=" + destinationType +
                ", statusOfEntity=" + statusOfEntity +
                '}';
    }
}
