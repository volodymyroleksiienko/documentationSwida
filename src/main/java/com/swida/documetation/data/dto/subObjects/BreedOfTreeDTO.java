package com.swida.documetation.data.dto.subObjects;

import com.swida.documetation.data.entity.subObjects.BreedOfTree;
import lombok.Data;

import javax.persistence.*;

@Data
public class BreedOfTreeDTO {
    private int id;
    private String breed;

    public static BreedOfTreeDTO convertToDTO(BreedOfTree breedOfTree){
        BreedOfTreeDTO  dto = new BreedOfTreeDTO();
        dto.id = breedOfTree.getId();
        dto.breed = breedOfTree.getBreed();
        return dto;
    }
}
