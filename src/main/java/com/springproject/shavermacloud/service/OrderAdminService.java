package com.springproject.shavermacloud.service;

import com.springproject.shavermacloud.repos.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderAdminService {

    private OrderRepository orderRepository;

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}
