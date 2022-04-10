package com.springproject.shavermacloud.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;       /*Внедряет MockMvc*/

//    @Test
//    public void testHomePage() throws Exception {
//        mockMvc.perform(get("/"))    /*Выполняет GET */
//                .andExpect(status().isOk())    /*-Ожидает HTTP 200*/
//                .andExpect(view().name("home"))    /*Ожидает home*/
//                .andExpect(content().string(containsString("Welcome to..."))); /*Отображаемое представление
//                 должно содержать текст Welcome to...*/
//    }



}


