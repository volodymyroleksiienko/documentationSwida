package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.entity.storages.DryStorage;
import lombok.Data;

import java.util.List;

@Data
public class DryStorageListDTO {
    private List<DryStorageDTO> list;

    public static DryStorageListDTO convertToDTO(List<DryStorage> dryStorages){
        DryStorageListDTO dto = new DryStorageListDTO();
        dto.list = DryStorageDTO.convertToDTO(dryStorages);
        return dto;
    }
}
