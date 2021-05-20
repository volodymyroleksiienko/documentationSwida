package com.swida.documetation.data.entity.storages;

import lombok.Data;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
    public String codeOfTeam="0";

    public String date=DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now());

    @ManyToOne
    private TreeStorage treeStorage;

    @OneToOne
    private RawStorage rawStorage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QualityStatisticInfo info = (QualityStatisticInfo) o;
        return Objects.equals(height, info.height) &&
                Objects.equals(firstExtent, info.firstExtent) &&
                Objects.equals(extent, info.extent) &&
                Objects.equals(percent, info.percent) &&
                Objects.equals(date, info.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(height, firstExtent, extent, percent, date);
    }

    @Override
    public String toString() {
        return "QualityStatisticInfo{" +
                "id=" + id +
                ", height='" + height + '\'' +
                ", firstExtent='" + firstExtent + '\'' +
                ", extent='" + extent + '\'' +
                ", percent='" + percent + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
