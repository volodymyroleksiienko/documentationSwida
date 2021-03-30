package com.swida.documetation.data.dto;

import com.swida.documetation.data.entity.storages.DryingStorage;
import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Data
public class CellDryingStorageDto {
    private Integer cell;
    private int countOfDesk;
    private String extent;
    private String startDate;
    private String endDate;
    private List<DryingStorage> dryingStorageList;


    public static List<CellDryingStorageDto> convert(List<DryingStorage> allObjects){
        List<CellDryingStorageDto> dtoList = new ArrayList<>();
        Set<Integer> uniqCells = new TreeSet<>();
        for (DryingStorage storage:allObjects) {
            if(storage.getCell()==null){
                storage.setCell(0);
            }
            uniqCells.add(storage.getCell());
        }

        for(Integer cell:uniqCells){
            CellDryingStorageDto dto = new CellDryingStorageDto();
            dto.cell=cell;
            dto.dryingStorageList=new ArrayList<>();

            float storageExtent=0;
            int storageDesk=0;
            for(DryingStorage storage:allObjects){
                if(storage.getCell().equals(cell)){
                    if(storage.getBreedOfTree().getId()==1){
                        storageDesk+=storage.getCountOfDesk();
                    }
                    storageExtent+=Float.parseFloat(storage.getExtent());
                    dto.endDate=storage.getDateDrying();
                    dto.startDate=storage.getStartDate();
                    dto.getDryingStorageList().add(storage);
                }
            }
            dto.extent=String.format("%.3f",storageExtent).replace(",",".");
            dto.countOfDesk=storageDesk;
            dtoList.add(dto);
        }
        return dtoList;
    }
}
