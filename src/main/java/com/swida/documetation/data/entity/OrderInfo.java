package com.swida.documetation.data.entity;

import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class OrderInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfOrder;
    private String breedOfTree;
    private String extentOfOrder;
    private String doneExtendOfOrder;
    private String toDoExtentOfOrder;
    @OneToOne
    private ContrAgent contrAgent;
    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
