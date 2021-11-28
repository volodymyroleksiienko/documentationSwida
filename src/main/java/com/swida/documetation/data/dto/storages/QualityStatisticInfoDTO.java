package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.entity.storages.QualityStatisticInfo;
import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
public class QualityStatisticInfoDTO {
    private int id;

    private String height="0";
    private String firstExtent="0";
    private String extent="0";
    private String percent="0";
    private String codeOfTeam="0";
    private String breedDescription;
    private String sizeOfWidth;
    private String sizeOfLong;
    private Integer countOfDesk;
    private String date=DateTimeFormatter.BASIC_ISO_DATE.format(LocalDate.now());
//    private TreeStorageDTO treeStorage;
    private RawStorageDTO rawStorage;

    public static QualityStatisticInfoDTO convertToDTO(QualityStatisticInfo info){
        QualityStatisticInfoDTO dto = new QualityStatisticInfoDTO();
        dto.id = info.getId();
        dto.height = info.getHeight();
        dto.firstExtent = info.getFirstExtent();
        dto.extent = info.getExtent();
        dto.percent = info.getPercent();
        dto.codeOfTeam = info.getCodeOfTeam();
        dto.breedDescription = info.getBreedDescription();
        dto.sizeOfWidth = info.getSizeOfWidth();
        dto.sizeOfLong = info.getSizeOfLong();
        dto.countOfDesk = info.getCountOfDesk();
        dto.date = info.getDate();
        if(info.getRawStorage()!=null){
            dto.rawStorage = RawStorageDTO.convertToDTO(info.getRawStorage());
        }
        return dto;
    }

    public static List<QualityStatisticInfoDTO> convertToDTO(List<QualityStatisticInfo> info){
        List<QualityStatisticInfoDTO> dto = new ArrayList<>();
        for(QualityStatisticInfo item : info){
            dto.add(QualityStatisticInfoDTO.convertToDTO(item));
        }
        return dto;
    }



    @Override
    public String toString() {
        return "QualityStatisticInfo{" +
                "id=" + id +
                ", height='" + height + '\'' +
                ", firstExtent='" + firstExtent + '\'' +
                ", extent='" + extent + '\'' +
                ", percent='" + percent + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}