package com.swida.documetation.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageItem{
    private String description;
    private int sizeOfHeight;
    private int sizeOfWidth;
    private int sizeOfLong;
    private double extent;
    private int countOfDesk;
}