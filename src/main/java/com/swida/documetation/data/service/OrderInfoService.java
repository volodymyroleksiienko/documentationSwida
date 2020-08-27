package com.swida.documetation.data.service;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfOrderInfo;

import java.util.List;

public interface OrderInfoService {
    void save(OrderInfo orderInfo);
    OrderInfo findById(int id);
    OrderInfo findByCodeOfOrder(String code);
    List<OrderInfo> getOrdersByStatusOfOrder(StatusOfOrderInfo status);
    List<OrderInfo> getOrdersByStatusOfOrderByDestination(StatusOfOrderInfo status, DeliveryDestinationType type);
    List<OrderInfo> getOrdersListByAgent(int agentId);
    List<OrderInfo> getOrdersListByAgentByBreed(int agentId,int breedId);
    List<OrderInfo> findAll();
    List<OrderInfo> findDistributionObj(int mainId);
    List<Integer> findDistributionId(int mainId);
    void changeDestinationTypeOfDistributed(OrderInfo main);
    void reloadOrderExtent(OrderInfo orderInfo, List<DeliveryDocumentation> docList);
    void reloadMainOrderExtent(OrderInfo orderInfo);
    void deleteByID(int id);
}
