package com.springproject.prodcloud.service;

import com.springproject.prodcloud.repos.OrderRepository;
import org.springframework.stereotype.Service;

@Service
public class OrderAdminService {

    private OrderRepository orderRepository;

    public void deleteAllOrders() {
        orderRepository.deleteAll();
    }
}
