package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {

    private Map<String,Order> orderMap=new HashMap<>();
    private Map<String,DeliveryPartner> deliveryPartnerMap=new HashMap<>();
    private Map<String, List<Order>> deliveryPartnerOrders=new HashMap<>();

    public void addOrder(Order order){
        orderMap.put(order.getId(),order);
    }
    public void addPartner(String pid){
        deliveryPartnerMap.put(pid,new DeliveryPartner(pid));
    }
    public void assignOrdertoPartner(String oid,String pid){
        if(orderMap.containsKey(oid) && deliveryPartnerMap.containsKey(pid)){
            List<Order> orders=new ArrayList<>();

            if(deliveryPartnerOrders.containsKey(pid))
            orders=deliveryPartnerOrders.get(pid);

                for (Order order : orders) {
                    if (order.getId().equals(oid)) return;  //Avoid duplicating Orders to One Partner...
                }

            orders.add(orderMap.get(oid));
            int noOfOrders=deliveryPartnerMap.get(pid).getNumberOfOrders();
            deliveryPartnerMap.get(pid).setNumberOfOrders(noOfOrders+1);
            deliveryPartnerOrders.put(pid,orders);
        }
    }
    public Order getOrderById(String oid){
        return orderMap.get(oid);
    }
    public DeliveryPartner getPartnerById(String pid){
        return deliveryPartnerMap.get(pid);
    }
    public Integer noOfOrdersWithPartner(String pid){

        return deliveryPartnerOrders.get(pid).size();
    }
    public List<String> getOrdersByPartnerId(String pid){

        if(deliveryPartnerOrders.containsKey(pid)) {  //See if this partner is there for delivery or not...
            List<String> orders = new ArrayList<>();

            if(deliveryPartnerOrders.get(pid)!=null && deliveryPartnerOrders.get(pid).size()>0){   //For Safety check if orders are assigned or not...
                for(Order order : deliveryPartnerOrders.get(pid)){
                    orders.add(order.getId());
                }
            }
            return orders;
        }
        return null;
    }
    public List<String> getAllOrders(){
        return new ArrayList<>(orderMap.keySet());
    }
    public Integer noOfUnassignedOrders(){
        Integer count=0;
        boolean found;

        for(String oid : orderMap.keySet()){
            found=false;
            for(List<Order> orders : deliveryPartnerOrders.values()){
                for(Order order : orders){
                    if(oid.equals(order.getId())) {
                        found = true;
                        break;
                    }
                }
                if(found) break;
            }
                if(!found)
                   count++;
        }
        return count;
    }
    public Integer noOfOrdersLeftAfterTime(String time,String pid){
        Integer count=0;
        int hours=Integer.parseInt(time.substring(0,2));
        int mins=Integer.parseInt(time.substring(3));
        int limit=(hours*60)+mins;

        if(deliveryPartnerOrders.get(pid)!=null && deliveryPartnerOrders.get(pid).size()>0) {
            for (Order order : deliveryPartnerOrders.get(pid)) {
                if (order.getDeliveryTime() > limit) count++;
            }
        }
        return count;
    }
    public String lastOrderOfPartner(String pid){
        String time="";
        int maxTime=0;
        for(Order order : deliveryPartnerOrders.get(pid)){
            if(order.getDeliveryTime()>maxTime) maxTime=order.getDeliveryTime();
        }
        double lastTime=0.0;
        lastTime=maxTime/60.0;

        lastTime=Math.round(lastTime*100.0)/100.0; //Nearest 2 decimal places according time...
        String res="";
        res=res+lastTime;

        int i;
        for(i=0;i<res.length();i++){
            if(res.charAt(i)!='.') time=time+res.charAt(i);
            else break;
        }
        if(time.length()==1) time='0'+time;

        time=time+":"+res.substring(i+1);

        return time;
    }
    public void deletePartnerById(String pid){
        deliveryPartnerMap.remove(pid);

        deliveryPartnerOrders.remove(pid);
    }
    public void deleteAndUnassignTheOrderFromPartner(String oid){
//        orderMap.remove(oid);
        int idx=-1,i;
        String pid=" ";

        for(String partner : deliveryPartnerMap.keySet()){
            if(deliveryPartnerOrders.containsKey(partner)) {
                for (Order order : deliveryPartnerOrders.get(partner)) {
                    if (order.getId().equals(oid)){
                        pid=partner;
                        break;
                    }
                }
            }
            if(!pid.equals(" ")) break;
        }

        for(List<Order> orderList : deliveryPartnerOrders.values()){
            for(i=0;i<orderList.size();i++){
                if(orderList.get(i).getId().equals(oid)){
                    idx=i;
                    break;
                }
            }
            if(idx!=-1) {
                orderList.remove(i);
                int noOfOrders=deliveryPartnerMap.get(pid).getNumberOfOrders();
                deliveryPartnerMap.get(pid).setNumberOfOrders(noOfOrders-1);
                break;
            }
        }
    }
}


