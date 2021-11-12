package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class QualityStatisticInfoListDTO {
    private List<QualityStatisticInfoDTO> list;
    public static QualityStatisticInfoListDTO convertToDTO(List<QualityStatisticInfo> infos){
        QualityStatisticInfoListDTO listDTO = new QualityStatisticInfoListDTO();
        listDTO.list = new ArrayList<>();
        if(infos!=null) {
            listDTO.list.addAll(QualityStatisticInfoDTO.convertToDTO(infos));
        }
        return listDTO;
    }
}
