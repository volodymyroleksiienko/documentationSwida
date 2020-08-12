package com.swida.documetation.data.serviceImpl;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.enums.StatusOfOrderInfo;
import com.swida.documetation.data.jpa.OrderInfoJPA;
import com.swida.documetation.data.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    private OrderInfoJPA orderInfoJPA;

    @Autowired
    public OrderInfoServiceImpl(OrderInfoJPA orderInfoJPA) {
        this.orderInfoJPA = orderInfoJPA;
    }

    @Override
    public void save(OrderInfo orderInfo) {
        orderInfo.setExtentOfOrder(String.format("%.3f", Float.parseFloat(orderInfo.getExtentOfOrder())).replace(',', '.'));
        orderInfo.setDoneExtendOfOrder(String.format("%.3f", Float.parseFloat(orderInfo.getDoneExtendOfOrder())).replace(',', '.'));
        orderInfo.setToDoExtentOfOrder(String.format("%.3f",Float.parseFloat(orderInfo.getExtentOfOrder())
            -Float.parseFloat(orderInfo.getDoneExtendOfOrder())));
        orderInfoJPA.save(orderInfo);
    }

    @Override
    public OrderInfo findById(int id) {
        return orderInfoJPA.getOne(id);
    }

    @Override
    public OrderInfo findByCodeOfOrder(String code) {
        return orderInfoJPA.findByCodeOfOrder(code);
    }

    @Override
    public List<OrderInfo> getOrdersByStatusOfOrder(StatusOfOrderInfo status) {
        return orderInfoJPA.getOrdersByStatusOfOrder(status);
    }

    @Override
    public List<OrderInfo> getOrdersListByAgent(int agentId) {
        return orderInfoJPA.getOrdersListByAgent(agentId);
    }

    @Override
    public List<OrderInfo> findAll() {
        return orderInfoJPA.findAll();
    }

    @Override
    public void deleteByID(int id) {
        orderInfoJPA.deleteById(id);
    }
}
