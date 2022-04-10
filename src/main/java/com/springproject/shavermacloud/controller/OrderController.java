package com.springproject.shavermacloud.controller;

import com.springproject.shavermacloud.domain.ProductOrder;
import com.springproject.shavermacloud.repos.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Slf4j
@Controller
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
    public String orderForm(@ModelAttribute("order") ProductOrder productOrder) {
        return "orderForm";
//        public String orderForm(Model model) {
//            model.addAttribute("productOrder", new ProductOrder());
//            return "orderForm";
    }

    @PostMapping("/orders")
    public String processOrder(@Valid @ModelAttribute("order") ProductOrder productOrder,
                               Errors errors,
                               SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        log.info("Order submitted: " + productOrder);
        orderRepo.save(productOrder);
        //Если вам необходимо уничтожить объекты в сессии, то это можно сделать
        // с помощью передачи в метод контроллера объекта SessionStatus sessionStatus,
        // и вызова у него метода setComplete();
        sessionStatus.setComplete();
        return "redirect:/";
    }


}


