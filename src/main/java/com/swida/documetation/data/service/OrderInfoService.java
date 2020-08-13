package com.swida.documetation.data.service;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.StatusOfOrderInfo;

import java.util.List;

public interface OrderInfoService {
    void save(OrderInfo orderInfo);
    OrderInfo findById(int id);
    OrderInfo findByCodeOfOrder(String code);
    List<OrderInfo> getOrdersByStatusOfOrder(StatusOfOrderInfo status);
    List<OrderInfo> getOrdersListByAgent(int agentId);
    List<OrderInfo> findAll();
    List<Integer> findDistributionId(int mainId);
    void deleteByID(int id);
}
