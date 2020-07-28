package com.swida.documetation.data.service;

import com.swida.documetation.data.entity.OrderInfo;

import java.util.List;

public interface OrderInfoService {
    void save(OrderInfo orderInfo);
    OrderInfo findById(int id);
    List<OrderInfo> findAll();
    void deleteByID(int id);
}
