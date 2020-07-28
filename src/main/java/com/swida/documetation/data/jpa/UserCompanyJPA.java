package com.swida.documetation.data.jpa;

import com.swida.documetation.data.entity.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCompanyJPA extends JpaRepository<UserCompany,Integer> {
    @Query("select u from UserCompany u where u.username=?1 group by u.username")
    UserCompany findByUsername(String username);
}
