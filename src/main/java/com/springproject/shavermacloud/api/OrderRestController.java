package com.springproject.shavermacloud.api;

import com.springproject.shavermacloud.domain.Order;
import com.springproject.shavermacloud.messaging.jms.services.JmsOrderMessagingService;
import com.springproject.shavermacloud.repos.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/orders", produces = "application/json")
@CrossOrigin(origins = "http://localhost:8080")
public class OrderRestController {

    private final OrderRepository orderRepo;
    private final JmsOrderMessagingService messageService;

    @Autowired
    public OrderRestController(OrderRepository orderRepo, JmsOrderMessagingService messageService) {
        this.orderRepo = orderRepo;
        this.messageService = messageService;
    }

    @GetMapping(produces = "application/json")
    public Iterable<Order> allOrders() {
        return orderRepo.findAll();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Order postOrder(@RequestBody Order order) {
        messageService.sendOrder(order);
        return orderRepo.save(order);
    }

    @PutMapping(path = "/{orderId}", consumes = "application/json")
    public Order putOrder(@PathVariable("orderId") Long orderId,
                          @RequestBody Order order) {
        order.setId(orderId);
        return orderRepo.save(order);
    }

    @PatchMapping(path = "/{orderId}", consumes = "application/json")
    public Order patchOrder(@PathVariable("orderId") Long orderId,
                            @RequestBody Order patch) {
        Optional<Order> optional = orderRepo.findById(orderId);
        Order order = new Order();

        if (optional.isPresent()) order = optional
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order don't patch"));

        if (patch.getName() != null) {
            order.setName(patch.getName());
        }
        if (patch.getStreet() != null) {
            order.setStreet(patch.getStreet());
        }
        if (patch.getCity() != null) {
            order.setCity(patch.getCity());
        }
        if (patch.getState() != null) {
            order.setState(patch.getState());
        }
        if (patch.getZip() != null) {
            order.setZip(patch.getZip());
        }
        if (patch.getCcNumber() != null) {
            order.setCcNumber(patch.getCcNumber());
        }
        if (patch.getCcExpiration() != null) {
            order.setCcExpiration(patch.getCcExpiration());
        }
        if (patch.getCcCVV() != null) {
            order.setCcCVV(patch.getCcCVV());
        }
        return orderRepo.save(order);
    }



    @DeleteMapping("/{orderId}")
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("orderId") Long orderId) {
        Optional<Order> opt = orderRepo.findById(orderId);
        if (opt.isPresent()) {
            orderRepo.deleteById(orderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
