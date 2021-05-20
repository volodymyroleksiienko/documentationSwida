package com.swida.documetation.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeStorageListDto {
    private String codeOfProduct;
    private int breedId;
    private int userId;
    private double extent;
    private double recycleExtent;
    private List<StorageItem> storageItems;
}
