package com.springproject.shavermacloud.controller;

import com.springproject.shavermacloud.domain.Order;
import com.springproject.shavermacloud.domain.User;
import com.springproject.shavermacloud.repos.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Slf4j
@Controller
@Transactional
//Если вам необходимо сохранить данные объекта (объект) между запросами,
// то необходимо поместить этот объект в сессию
@SessionAttributes(value = "order")
public class OrderController {

    private OrderRepository orderRepo;

    @Autowired
    public OrderController(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }


    @GetMapping("/orders/current")
    public String orderForm(@AuthenticationPrincipal User user,
                            @ModelAttribute("order") Order order) {
        if (order.getName() == null) {
            order.setName(user.getFullname());
        }
        if (order.getState() == null) {
            order.setState(user.getStreet());
        }
        if (order.getCity() == null) {
            order.setCity(user.getCity());
        }
        if (order.getState() == null) {
            order.setState(user.getState());
        }
        if (order.getZip() == null) {
            order.setZip(user.getZip());
        }
        return "orderForm";
    }

    @PostMapping("/orders")
    public String processOrder(@Valid @ModelAttribute("order") Order order,
                               Errors errors,
                               @AuthenticationPrincipal User user,
                               SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        log.info("Order submitted: " + order);
        order.setUser(user);
        orderRepo.save(order);
        //Если вам необходимо уничтожить объекты в сессии, то это можно сделать
        // с помощью передачи в метод контроллера объекта SessionStatus sessionStatus,
        // и вызова у него метода setComplete();
        sessionStatus.setComplete();
        return "redirect:/";
    }


//    @GetMapping
//    public String ordersForUser(
//            @AuthenticationPrincipal User user, Model model) {
//
//        Pageable pageable = PageRequest.of(0, props.getPageSize());
//        model.addAttribute("orders",
//                orderRepo.findByUserProductOrderByCreatedAtDesc(user, pageable));
//
//        return "orderList";
//    }


}


