package com.swida.documetation.data.jpa;

import com.swida.documetation.data.entity.UserCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCompanyJPA extends JpaRepository<UserCompany,Integer> {
}
