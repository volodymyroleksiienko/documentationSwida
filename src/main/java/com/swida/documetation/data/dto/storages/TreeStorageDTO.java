package com.swida.documetation.data.dto.storages;

import com.swida.documetation.data.dto.subObjects.BreedOfTreeDTO;
import com.swida.documetation.data.dto.subObjects.ContrAgentDTO;
import com.swida.documetation.data.entity.storages.TreeStorage;
import lombok.Data;

import java.util.List;

@Data
public class TreeStorageDTO {
    private int id;
    private String codeOfProduct;
    private String breedDescription="";
    private String extent="0.0";
    private String maxExtent="0.0";
    private String date;
    private String averageDiameter;
    private String countOfDeck;
    private String treeDescription;
    private Boolean isMainStorage;
    private List<QualityStatisticInfoDTO> statisticInfoList;
//    private List<TreeStorageDTO> recycle;
//    private List<RawStorageDTO> rawStorageList;
    private BreedOfTreeDTO breedOfTree;
//    private OrderInfoDTO orderInfo;
    private ContrAgentDTO contrAgent;
//    private UserCompanyDTO userCompany;
//    private StatusOfTreeStorage statusOfTreeStorage = StatusOfTreeStorage.TREE;
//    private StatusOfEntity statusOfEntity = StatusOfEntity.ACTIVE;


    public static TreeStorageDTO convertToDTO(TreeStorage treeStorage){
        TreeStorageDTO dto = new TreeStorageDTO();
        dto.id=treeStorage.getId();
        dto.codeOfProduct=treeStorage.getCodeOfProduct();
        dto.breedDescription=treeStorage.getBreedDescription();
        dto.extent=treeStorage.getExtent();
        dto.maxExtent=treeStorage.getMaxExtent();
        dto.date=treeStorage.getDate();
        dto.averageDiameter=treeStorage.getAverageDiameter();
        dto.countOfDeck=treeStorage.getCountOfDeck();
        dto.treeDescription=treeStorage.getTreeDescription();
        dto.isMainStorage=treeStorage.getIsMainStorage();
        dto.breedOfTree = BreedOfTreeDTO.convertToDTO(treeStorage.getBreedOfTree());
        if(treeStorage.getContrAgent()!=null) {
            dto.contrAgent = ContrAgentDTO.convertToDTO(treeStorage.getContrAgent());
        }
        return dto;
    }
}
