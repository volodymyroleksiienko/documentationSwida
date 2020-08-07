package com.swida.documetation.data.entity;

import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.StatusOfEntity;
import com.swida.documetation.data.enums.StatusOfOrderInfo;
import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Entity
@Data
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfOrder;
    private String extentOfOrder;
    private String doneExtendOfOrder;
    private String toDoExtentOfOrder;

    @OneToMany
    private List<OrderInfo> distributionList;
    @ManyToOne
    private BreedOfTree breedOfTree;
    @OneToOne
    private ContrAgent contrAgent;
    @Enumerated(EnumType.STRING)
    private StatusOfOrderInfo statusOfOrderInfo;
    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
