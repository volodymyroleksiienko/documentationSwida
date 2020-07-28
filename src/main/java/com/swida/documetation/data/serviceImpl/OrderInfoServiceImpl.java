package com.swida.documetation.data.serviceImpl;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.jpa.OrderInfoJPA;
import com.swida.documetation.data.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        orderInfoJPA.save(orderInfo);
    }

    @Override
    public OrderInfo findById(int id) {
        return orderInfoJPA.getOne(id);
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
