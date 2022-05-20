package com.springproject.shavermacloud.controller;

import com.springproject.shavermacloud.domain.Order;
import com.springproject.shavermacloud.domain.Provider;
import com.springproject.shavermacloud.messaging.OrderReceiver;
import com.springproject.shavermacloud.service.OrderProps;
import com.springproject.shavermacloud.domain.User;
import com.springproject.shavermacloud.repos.OrderRepository;
import com.springproject.shavermacloud.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Profile({"prod","dev"})
@Controller
@RequestMapping("/orders")
//Если вам необходимо сохранить данные объекта (объект) между запросами,
// то необходимо поместить этот объект в сессию
@SessionAttributes(value = "order")
public class OrderController {

    private OrderProps props;
    private OrderRepository orderRepo;
    private UserRepository userRepository;
    private OrderReceiver orderReceiver;

    @Autowired
    public OrderController(OrderRepository orderRepo, UserRepository userRepository, OrderProps props, OrderReceiver orderReceiver) {
        this.orderRepo = orderRepo;
        this.userRepository = userRepository;
        this.props = props;
        this.orderReceiver = orderReceiver;
    }


    @GetMapping("/current")
    public String orderForm(@ModelAttribute("order") Order order) {
        User user = order.getUser();

        if (user.getProvider() == Provider.LOCAL) {
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
        }
        return "orderForm";
    }

    @PostMapping
    public String processOrder(@Valid @ModelAttribute("order") Order order,
                               Errors errors,
                               @AuthenticationPrincipal User user) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        log.info("Order submitted: " + order);
        order.setUser(user);
        orderRepo.save(order);
        //Если вам необходимо уничтожить объекты в сессии, то это можно сделать
        // с помощью передачи в метод контроллера объекта SessionStatus sessionStatus,
        // и вызова у него метода setComplete();

        return "redirect:/";
    }


    @GetMapping
    public String ordersForUser(@AuthenticationPrincipal User user,
                                Model model) {
        Pageable pageable = PageRequest.of(0, props.getPageSize());// ограничить количество отображаемых заказов самыми последними 20 заказами
        model.addAttribute("orders", orderRepo.findByUserOrderByCreatedAtDesc(user, pageable));

        return "orderList";
    }




    @GetMapping("/receive")
    public String receiveOrder(Model model, SessionStatus sessionStatus) {
        Order order = orderReceiver.receiveOrder();
        if (order != null) {
            model.addAttribute("order", order);
            return "receiveOrder";
        }
        sessionStatus.setComplete();
        return "noOrder";
    }



}


