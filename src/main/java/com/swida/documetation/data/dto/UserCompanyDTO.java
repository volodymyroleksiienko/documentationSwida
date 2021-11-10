package com.swida.documetation.data.dto;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.Roles;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class UserCompanyDTO{
    private int id;

    private String nameOfCompany;
    private String location;
    private String username;
    private String password;
    private Roles role;

//    private ContrAgent contrAgent;
//    private List<TreeStorage> treeStorageList;

    public static UserCompanyDTO convertToDTO(UserCompany userCompany){
        UserCompanyDTO dto = new UserCompanyDTO();
        if(userCompany!=null) {
            dto.id = userCompany.getId();
            dto.nameOfCompany = userCompany.getNameOfCompany();
            dto.location = userCompany.getLocation();
            dto.username = userCompany.getUsername();
            dto.password = userCompany.getPassword();
            dto.role = userCompany.getRole();
        }
        return dto;
    }
}
