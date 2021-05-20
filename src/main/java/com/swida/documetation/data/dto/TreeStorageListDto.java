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
    private double extent;
    private double recycleExtent;
    private List<StorageItem> storageItems;


}
@Data
@AllArgsConstructor
@NoArgsConstructor
class StorageItem{
    private String description;
    private int sizeOfHeight;
    private int sizeOfWidth;
    private int sizeOfLong;
    private double extent;
    private int countOfDesk;
}
