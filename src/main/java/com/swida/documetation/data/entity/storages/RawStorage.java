package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Data
public class RawStorage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codeOfProduct;
    @ManyToOne
    private BreedOfTree breedOfTree;
    private String breedDescription="";

    private String sizeOfHeight;
    private String sizeOfWidth="0";
    private String sizeOfLong="0";

    private int countOfDesk;
    private int maxCountOfDesk;

    private String extent="0.0";
    private String maxExtent="0.0";
    private String usedExtent="0.0";
    private String description;
    private String date;

    @OneToOne(cascade = CascadeType.ALL)
    private QualityStatisticInfo statisticInfo;

    @OneToMany
    private List<RawStorage> groupedElements;

    @OneToMany(mappedBy = "rawStorage",cascade = CascadeType.ALL)
    private List<DescriptionDeskOak> deskOakList;

    @ManyToOne
    private UserCompany userCompany;

    @ManyToOne
    private TreeStorage treeStorage;

    @OneToMany(mappedBy = "rawStorage",cascade = CascadeType.ALL)
    private List<DryingStorage> dryingStorageList;

    @OneToOne
    private TreeStorage recycle;

    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RawStorage that = (RawStorage) o;
        return id == that.id &&
                countOfDesk == that.countOfDesk &&
                maxCountOfDesk == that.maxCountOfDesk &&
                Objects.equals(codeOfProduct, that.codeOfProduct) &&
                Objects.equals(breedOfTree, that.breedOfTree) &&
                Objects.equals(breedDescription, that.breedDescription) &&
                Objects.equals(sizeOfHeight, that.sizeOfHeight) &&
                Objects.equals(sizeOfWidth, that.sizeOfWidth) &&
                Objects.equals(sizeOfLong, that.sizeOfLong) &&
                Objects.equals(extent, that.extent) &&
                Objects.equals(maxExtent, that.maxExtent) &&
                Objects.equals(usedExtent, that.usedExtent) &&
                Objects.equals(description, that.description) &&
                Objects.equals(date, that.date) &&
                statusOfEntity == that.statusOfEntity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codeOfProduct, breedOfTree, breedDescription, sizeOfHeight, sizeOfWidth, sizeOfLong, countOfDesk, maxCountOfDesk, extent, maxExtent, usedExtent, description, date, statusOfEntity);
    }

    @Override
    public String toString() {
        return "RawStorage{" +
                "id=" + id +
                ", codeOfProduct='" + codeOfProduct + '\'' +
                ", breedOfTree=" + breedOfTree +
                ", breedDescription='" + breedDescription + '\'' +
                ", sizeOfHeight='" + sizeOfHeight + '\'' +
                ", sizeOfWidth='" + sizeOfWidth + '\'' +
                ", sizeOfLong='" + sizeOfLong + '\'' +
                ", countOfDesk=" + countOfDesk +
                ", maxCountOfDesk=" + maxCountOfDesk +
                ", extent='" + extent + '\'' +
                ", maxExtent='" + maxExtent + '\'' +
                ", usedExtent='" + usedExtent + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
//                ", deskOakList=" + deskOakList +
                ", userCompany=" + userCompany +
//                ", treeStorage=" + treeStorage +
                ", dryingStorageList=" + dryingStorageList +
                ", recycle=" + recycle +
                ", statusOfEntity=" + statusOfEntity +
                '}';
    }
}
