package com.swida.documetation.data.entity;

import com.swida.documetation.data.enums.Roles;
import lombok.Data;


import javax.persistence.*;

@Entity
@Data
public class UserCompany {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nameOfCompany;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Roles role;
}
