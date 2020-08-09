package com.swida.documetation.data.serviceImpl;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.jpa.UserCompanyJPA;
import com.swida.documetation.data.service.UserCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserCompanyServiceImpl implements UserCompanyService {
    UserCompanyJPA userCompanyJPA;

    @Autowired
    public UserCompanyServiceImpl(UserCompanyJPA userCompanyJPA) {
        this.userCompanyJPA = userCompanyJPA;
    }

    @Override
    public void save(UserCompany userComp) {
        userCompanyJPA.save(userComp);
    }

    @Override
    public UserCompany findById(int id) {
        return userCompanyJPA.getOne(id);
    }

    @Override
    public UserCompany findByUsername(String username) {
        return userCompanyJPA.findByUsername(username);
    }

    @Override
    public List<UserCompany> getListOfAllUsersROLE() {
        return userCompanyJPA.getListOfAllUsersROLE();
    }

    @Override
    public List<UserCompany> findAll() {
        return userCompanyJPA.findAll();
    }

    @Override
    public void deleteByID(int id) {
        userCompanyJPA.deleteById(id);
    }
}
