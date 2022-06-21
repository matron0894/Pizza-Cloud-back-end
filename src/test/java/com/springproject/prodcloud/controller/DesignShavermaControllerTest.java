package com.springproject.prodcloud.controller;

import com.springproject.prodcloud.repos.IngredientRepository;
import com.springproject.prodcloud.repos.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WebMvcTest(DesignShavermaController.class)
public class DesignShavermaControllerTest {

    @Autowired
    private MockMvc mockMvc;       /*Внедряет MockMvc*/


    @MockBean
    private IngredientRepository ingredientRepository;

    @MockBean
    private ProductRepository productRepository;

    @Test
    @WithMockUser(username = "user1", password = "pwd", roles = "USER")
    public void testDesignPage() throws Exception {
        mockMvc.perform(get("/design"))    /*Выполняет GET */
                .andExpect(status().isOk())    /*-Ожидает HTTP 200*/
                .andExpect(view().name("design"))    /*Ожидает home*/
                .andExpect(content().string(containsString("Design your shaverma!"))); /*Отображаемое представление
                 должно содержать текст Welcome to...*/
    }
}
