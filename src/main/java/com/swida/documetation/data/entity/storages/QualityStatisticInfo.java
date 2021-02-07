package com.swida.documetation.data.entity.storages;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class QualityStatisticInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public String height="0";
    public String extent="0";
    public String percent="0";


    @ManyToOne
    private TreeStorage treeStorage;
}
