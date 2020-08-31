package com.swida.documetation.data.entity.subObjects;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class DeliveryDocumentation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String dateOfUnloading;
    private String timeOfUnloading;
    private String clientName;
    private String description;
    private String packagesExtent;


    @ManyToOne
    private OrderInfo orderInfo;
    @ManyToOne
    private BreedOfTree breedOfTree;
    @ManyToOne
    private DriverInfo driverInfo;
    @ManyToOne
    private ContrAgent contrAgent;
    @ManyToOne
    private UserCompany userCompany;
    @OneToMany(fetch = FetchType.LAZY)
    private List<PackagedProduct> productList;
    @Enumerated(EnumType.STRING)
    private DeliveryDestinationType destinationType;
    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
