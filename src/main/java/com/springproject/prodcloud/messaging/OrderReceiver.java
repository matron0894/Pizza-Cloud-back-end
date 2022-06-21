package com.springproject.prodcloud.messaging;

import com.springproject.prodcloud.domain.Order;

public interface OrderReceiver {

  Order receiveOrder();

}