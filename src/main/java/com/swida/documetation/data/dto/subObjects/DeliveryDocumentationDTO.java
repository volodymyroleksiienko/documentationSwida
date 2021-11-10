package com.swida.documetation.data.dto.subObjects;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.PackagedProduct;
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

    private OrderInfo orderInfo;
    private BreedOfTreeDTO breedOfTree;
    private DriverInfoDTO driverInfo;
    private ContrAgentDTO contrAgent;
    private UserCompany userCompany;
    private List<PackagedProduct> productList;
    private DeliveryDestinationType destinationType;
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

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
