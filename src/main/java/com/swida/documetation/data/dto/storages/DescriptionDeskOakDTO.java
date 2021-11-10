package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.enums.StatusOfEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
public class DescriptionDeskOakDTO {
    private int id;
    private String sizeOfWidth;
    private String countOfDesk;

    private DryingStorageDTO dryingStorage;
    private DryStorageDTO dryStorage;
    private RawStorageDTO rawStorage;
    private PackagedProductDTO packagedProduct;
    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;


}
