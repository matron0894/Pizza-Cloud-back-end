package com.springproject.prodcloud.messaging;


import com.springproject.prodcloud.domain.Order;

public interface OrderSender {

  void sendOrder(Order order);
  
}
