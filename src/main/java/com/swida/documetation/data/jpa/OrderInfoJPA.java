package com.swida.documetation.data.jpa;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfOrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;

public interface OrderInfoJPA extends JpaRepository<OrderInfo,Integer> {
    @Query("select obj from OrderInfo obj where obj.statusOfOrderInfo=?1")
    List<OrderInfo> getOrdersByStatusOfOrder(StatusOfOrderInfo status);

    @Query("select obj from OrderInfo obj where obj.statusOfOrderInfo=?1 and  obj.destinationType=?2")
    List<OrderInfo> getOrdersByStatusOfOrderByDestination(StatusOfOrderInfo status, DeliveryDestinationType type);

    @Query("select obj from OrderInfo  obj where obj.contrAgent.id=?1")
    List<OrderInfo> getOrdersListByAgent(int agentId);

    @Query("select obj from  OrderInfo obj where obj.codeOfOrder=?1")
    OrderInfo findByCodeOfOrder(String code);

    @Query("select obj.id from  OrderInfo obj where obj.mainOrder.id=?1")
    List<Integer> findDistributionId(int mainId);

    @Query("select obj from  OrderInfo obj where obj.mainOrder.id=?1")
    List<OrderInfo> findDistributionObj(int mainId);
}
