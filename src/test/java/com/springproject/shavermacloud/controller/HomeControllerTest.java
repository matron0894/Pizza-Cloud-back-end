package com.springproject.shavermacloud.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.springproject.shavermacloud.domain.User;
import com.springproject.shavermacloud.repos.IngredientRepository;
import com.springproject.shavermacloud.repos.OrderRepository;
import com.springproject.shavermacloud.repos.ProductRepository;
import com.springproject.shavermacloud.repos.UserRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


@RunWith(SpringRunner.class)
@ContextConfiguration
@WebMvcTest(HomeController.class)
//@Ignore("Unsure how to test with @DataJpaTest")
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;       /*Внедряет MockMvc*/

    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private ProductRepository designRepository;

    @MockBean
    private OrderRepository orderRepository;


    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void testHomePage() throws Exception {
        mockMvc.perform(get("/"))    /*Выполняет GET */
                .andExpect(status().isOk())    /*-Ожидает HTTP 200*/
                .andExpect(view().name("home"))    /*Ожидает home*/
                .andExpect(content().string(containsString("Welcome to..."))); /*Отображаемое представление
                 должно содержать текст Welcome to...*/
    }


}


