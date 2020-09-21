package com.swida.documetation.data.serviceImpl;

import com.swida.documetation.data.entity.OrderInfo;
import com.swida.documetation.data.entity.storages.PackagedProduct;
import com.swida.documetation.data.entity.subObjects.ContrAgent;
import com.swida.documetation.data.entity.subObjects.DeliveryDocumentation;
import com.swida.documetation.data.enums.DeliveryDestinationType;
import com.swida.documetation.data.enums.StatusOfOrderInfo;
import com.swida.documetation.data.jpa.OrderInfoJPA;
import com.swida.documetation.data.jpa.subObjects.DeliveryDocumentationJPA;
import com.swida.documetation.data.service.OrderInfoService;
import com.swida.documetation.data.service.subObjects.DeliveryDocumentationService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    private OrderInfoJPA orderInfoJPA;
    private DeliveryDocumentationJPA deliveryDocumentationJPA;

    @Autowired
    public OrderInfoServiceImpl(OrderInfoJPA orderInfoJPA,DeliveryDocumentationJPA deliveryDocumentationJPA) {
        this.orderInfoJPA = orderInfoJPA;
        this.deliveryDocumentationJPA = deliveryDocumentationJPA;
    }

    @Override
    public void save(OrderInfo orderInfo) {
        orderInfo.setExtentOfOrder(String.format("%.3f", Float.parseFloat(orderInfo.getExtentOfOrder())).replace(',', '.'));
        orderInfo.setDoneExtendOfOrder(String.format("%.3f", Float.parseFloat(orderInfo.getDoneExtendOfOrder())).replace(',', '.'));
        orderInfo.setToDoExtentOfOrder(String.format("%.3f",Float.parseFloat(orderInfo.getExtentOfOrder())
            -Float.parseFloat(orderInfo.getDoneExtendOfOrder())).replace(",","."));
        if(orderInfo.getBreedDescription().codePoints().allMatch(Character::isWhitespace)){
            orderInfo.setBreedDescription("");
        }
        if(orderInfo.getStatusOfOrderInfo()==StatusOfOrderInfo.MAIN){
            orderInfo.setExtentWithoutContainer(
                    String.format("%.3f",
                            Float.parseFloat(orderInfo.getDoneExtendOfOrder())-Float.parseFloat(orderInfo.getExtentInContainer())).replace(",",".")
            );
        }
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
    public List<OrderInfo> getOrdersByStatusOfOrderByDestination(StatusOfOrderInfo status, DeliveryDestinationType type) {
        return orderInfoJPA.getOrdersByStatusOfOrderByDestination(status,type);
    }

    @Override
    public List<OrderInfo> getOrdersByStatusOfOrderByDestinationOnlyActive(StatusOfOrderInfo status, DeliveryDestinationType type) {
        return orderInfoJPA.getOrdersByStatusOfOrderByDestinationOnlyActive(status, type);
    }

    @Override
    public List<OrderInfo> getOrdersListByAgent(int agentId) {
        return orderInfoJPA.getOrdersListByAgent(agentId);
    }

    @Override
    public List<OrderInfo> getOrdersListByBreed(int breedId) {
        return orderInfoJPA.getOrdersListByBreed(breedId);
    }

    @Override
    public List<OrderInfo> getOrdersListByAgentByBreed(int agentId, int breedId) {
        return orderInfoJPA.getOrdersListByAgentByBreed(agentId,breedId);
    }

    @Override
    public List<OrderInfo> findAll() {
        return orderInfoJPA.findAll();
    }

    @Override
    public List<OrderInfo> findDistributionObj(int mainId) {
        return orderInfoJPA.findDistributionObj(mainId);
    }

    @Override
    public List<Integer> findDistributionId(int mainId) {
        return orderInfoJPA.findDistributionId(mainId);
    }

    @Override
    public void changeDestinationTypeOfDistributed(OrderInfo main) {
        List<OrderInfo> list = orderInfoJPA.findDistributionObj(main.getId());
        for(OrderInfo orderInfo:list){
            orderInfo.setDestinationType(main.getDestinationType());
        }
        orderInfoJPA.saveAll(list);
    }

    @Override
    public void reloadOrderExtent(OrderInfo orderInfo,List<DeliveryDocumentation> docList) {
            float fullExtent = 0;
            for (DeliveryDocumentation doc : docList) {
                fullExtent += Float.parseFloat(doc.getPackagesExtent());
            }
            orderInfo.setDoneExtendOfOrder(String.format("%.3f", fullExtent).replace(',', '.'));
            save(orderInfo);
    }

    @Override
    public void reloadMainOrderExtent(OrderInfo orderInfo) {
        List<OrderInfo> list = orderInfoJPA.findDistributionObj(orderInfo.getId());
        float fullExtent = 0;
        for (OrderInfo order:list){
            fullExtent += Float.parseFloat(order.getDoneExtendOfOrder());
        }
        orderInfo.setDoneExtendOfOrder(String.format("%.3f", fullExtent).replace(',', '.'));
        save(orderInfo);
    }

    @Override
    public void reloadExtentInContainer(OrderInfo mainOrder) {
        float extent = 0;
        List<DeliveryDocumentation> docList = deliveryDocumentationJPA.getListByDistributionContractsId(
                orderInfoJPA.findDistributionId(mainOrder.getId())
        );



        for(DeliveryDocumentation doc:docList){
            for (PackagedProduct product:doc.getProductList()){
                if(product.getContainer()!=null){
                    extent += Float.parseFloat(product.getExtent());
                }
            }
        }
        mainOrder.setExtentInContainer(
                String.format("%.3f",extent).replace(",",".")
        );
        mainOrder.setExtentWithoutContainer(
                String.format("%.3f",
                        Float.parseFloat(mainOrder.getDoneExtendOfOrder())-extent).replace(",",".")
        );
        orderInfoJPA.save(mainOrder);
    }

    @Override
    public void checkLeftOverInfo(int contractId) {
        OrderInfo mainOrder = orderInfoJPA.getOne(contractId);
        List<OrderInfo> distributedOrders = orderInfoJPA.findDistributionObj(contractId);
    }



    @Override
    public void deleteByID(int id) {
        orderInfoJPA.deleteById(id);
    }

    @Override
    public void deleteEmptyLeftOverOrders(List<OrderInfo> orderList) {
        for(OrderInfo order:orderList){
            if (Float.parseFloat(order.getDoneExtendOfOrder())==0){
               List<OrderInfo> distributionOrders = orderInfoJPA.findDistributionObj(order.getId());
               for(OrderInfo distribution:distributionOrders){
                   orderInfoJPA.deleteById(distribution.getId());
               }
                orderInfoJPA.deleteById(order.getId());
            }
        }
    }

    @Override
    public List<String> getListOfUnicBreedDescription(int breedId) {
        return orderInfoJPA.getListOfUnicBreedDescription(breedId);
    }

    @Override
    public List<String> getExtentProviderInWork(int breedId, int[] agentId) {
        return orderInfoJPA.getExtentProviderInWork(breedId,agentId);
    }
}
