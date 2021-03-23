package com.swida.documetation.data.entity.storages;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Data
public class QualityStatisticInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public String height="0";
    public String firstExtent="0";
    public String extent="0";
    public String percent="0";

    public String date=DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now());

    @ManyToOne
    private TreeStorage treeStorage;
}
