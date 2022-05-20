package com.springproject.shavermacloud.messaging;

import com.springproject.shavermacloud.domain.Order;

public interface OrderReceiver {

  Order receiveOrder();

}