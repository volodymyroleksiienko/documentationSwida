package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.entity.storages.PackagedProduct;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PackageProductListDTO {
    private List<PackagedProductDTO> list;

    public static PackageProductListDTO convertToDTO(List<PackagedProduct> list){
        PackageProductListDTO dto = new PackageProductListDTO();
        dto.list = new ArrayList<>();
        dto.list.addAll(PackagedProductDTO.convertToDTO(list));
        return dto;
    }
}

