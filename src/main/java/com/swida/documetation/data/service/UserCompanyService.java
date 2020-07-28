package com.swida.documetation.data.service;

import com.swida.documetation.data.entity.UserCompany;

import java.util.List;

public interface UserCompanyService {
    void save(UserCompany userComp);
    UserCompany findById(int id);
    UserCompany findByUsername(String username);
    List<UserCompany> findAll();
    void deleteByID(int id);
}
