package com.swida.documetation.data.jpa;

import com.swida.documetation.data.entity.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderInfoJPA extends JpaRepository<OrderInfo,Integer> {
}
