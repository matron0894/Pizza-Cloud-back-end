package com.springproject.prodcloud.messaging.jms.listener;

import com.springproject.prodcloud.domain.Order;
import com.springproject.prodcloud.messaging.KitchenUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class OrderListener {
  
  private KitchenUI ui;

  @Autowired
  public OrderListener(KitchenUI ui) {
    this.ui = ui;
  }

   @JmsListener(destination = "${prodcloud.order.queue}")
  public void receiveOrder(Order order) {
    ui.displayOrder(order);
  }
  
}
