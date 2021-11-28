package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.entity.storages.RawStorage;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RawStorageListDTO {
    private List<RawStorageDTO> list;

    public static RawStorageListDTO convertToDTO(List<RawStorage> rawStorages){
        RawStorageListDTO dto = new RawStorageListDTO();
        dto.list = new ArrayList<>();
        dto.list = RawStorageDTO.convertToDTO(rawStorages);
        return dto;
    }
}
