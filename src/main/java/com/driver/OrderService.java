package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public void addOrder(Order order){
        orderRepository.addOrder(order);
    }
    public void addPartner(String pid){
        orderRepository.addPartner(pid);
    }
    public void assignOrderToPartner(String oid,String pid){
        orderRepository.assignOrdertoPartner(oid,pid);
    }
    public Order getOrderById(String oid){
        return orderRepository.getOrderById(oid);
    }
    public DeliveryPartner getPartnerById(String pid){
        return orderRepository.getPartnerById(pid);
    }
    public Integer noOfOrdersWithPartner(String pid){
        return orderRepository.noOfOrdersWithPartner(pid);
    }
    public List<String> getOrdersByPartnerId(String pid){
        return orderRepository.getOrdersByPartnerId(pid);
    }
    public List<String> getAllOrders(){
        return orderRepository.getAllOrders();
    }
    public Integer noOfUnassignedOrders(){
        return orderRepository.noOfUnassignedOrders();
    }
    public Integer noOfOrdersLeftAfterTime(String time,String pid){
        return orderRepository.noOfOrdersLeftAfterTime(time, pid);
    }
    public String lastOrderOfPartner(String pid){
        return orderRepository.lastOrderOfPartner(pid);
    }
    public void deletePartnerById(String pid){
        orderRepository.deletePartnerById(pid);
    }
    public void deleteAndUnassignOrderFromPartner(String oid){
        orderRepository.deleteAndUnassignTheOrderFromPartner(oid);
    }
}
