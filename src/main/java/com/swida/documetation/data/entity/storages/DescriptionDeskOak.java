package com.swida.documetation.data.entity.storages;

import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DescriptionDeskOak {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public DescriptionDeskOak(String sizeOfWidth,String countOfDesk) {
        this.sizeOfWidth = sizeOfWidth;
        this.countOfDesk = countOfDesk;
    }

    private String sizeOfWidth;
    private String countOfDesk;

    @ManyToOne
    private DryingStorage dryingStorage;

    @ManyToOne
    private DryStorage dryStorage;

    @ManyToOne
    private RawStorage rawStorage;

    @ManyToOne
    private PackagedProduct packagedProduct;

    @Enumerated(EnumType.STRING)
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;
}
