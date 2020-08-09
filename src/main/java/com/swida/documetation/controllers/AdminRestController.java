package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.enums.StatusOfOrderInfo;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.subObjects.ContrAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class AdminRestController {
    private OrderInfoService orderInfoService;
    private ContrAgentService contrAgentService;

    @Autowired
    public AdminRestController(OrderInfoService orderInfoService,ContrAgentService contrAgentService) {
        this.orderInfoService = orderInfoService;
        this.contrAgentService = contrAgentService;
    }

    @PostMapping("/createDistribution")
    public void createDistribution(@RequestParam("suppliersList") String[] arrayOfAgent,
                                 @RequestParam("orderingsList") String[] arrayOfExtent, @RequestParam("id") String idOfMainOrder){
        System.out.println("main order id "+idOfMainOrder);
        OrderInfo main = orderInfoService.findById(Integer.parseInt(idOfMainOrder));
        List<OrderInfo> orderInfoList = new ArrayList<>();

        float sumOfExtent = 0;
        for (int i=0; i<arrayOfAgent.length; i++){
            sumOfExtent+= Float.parseFloat(arrayOfExtent[i]);

            OrderInfo order = new OrderInfo();
            order.setCodeOfOrder(main.getCodeOfOrder());
            order.setBreedOfTree(main.getBreedOfTree());
            order.setContrAgent(contrAgentService.getObjectByName(arrayOfAgent[i]));
            order.setExtentOfOrder(arrayOfExtent[i]);
            order.setDoneExtendOfOrder("0.000");
            order.setStatusOfOrderInfo(StatusOfOrderInfo.DISTRIBUTION);
            order.setMainOrder(main);
            orderInfoService.save(order);
            orderInfoList.add(order);
        }

        main.setExtentForDistribution(String.valueOf(Float.parseFloat(main.getExtentForDistribution())-sumOfExtent));
        orderInfoService.save(main);
    }
}
