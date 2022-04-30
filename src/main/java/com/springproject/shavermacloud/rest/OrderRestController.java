package com.springproject.shavermacloud.rest;

import com.springproject.shavermacloud.domain.Order;
import com.springproject.shavermacloud.domain.Product;
import com.springproject.shavermacloud.repos.OrderRepository;
import com.springproject.shavermacloud.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/api/product", produces = "application/json")
@CrossOrigin(origins = "http://tacocloud:8080")
public class OrderRestController {

    private final OrderRepository orderRepo;

    @Autowired
    public OrderRestController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
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
        Order order = orderRepo.findById(orderId).get();
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<HttpStatus> deleteOrder(@PathVariable("orderId") Long orderId) {
        Optional<Order> opt = orderRepo.findById(orderId);
        if (opt.isPresent()) {
            orderRepo.deleteById(orderId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
