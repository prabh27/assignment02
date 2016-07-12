package com.cnu2016.controller;

import com.cnu2016.model.Orders;
import junit.framework.TestCase;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;



import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by prabh on 12/07/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
@TestPropertySource("classpath:test-application.properties")
public class OrdersControllerTest extends TestCase{
    @Configuration
    static class ContextConfiguration {
        @Bean
        private Orders mockOrders;

    }


    private Orders mockOrders;

    private MockMvc mockMvc;


    @Override
    protected void setUp() throws Exception {
        System.out.println("setting up");
        mockOrders = mock(Orders.class);
    }

    @Test
    public void testPostOrder() throws Exception {

       // assertNotNull(mockOrders);
        MockHttpServletRequestBuilder postRequest = post("/api/orders");
//
        ResultActions resultActions = mockMvc.perform(postRequest);
        resultActions.andExpect(status().isOk());

//        mockMvc.perform(post("/api/orders")
//                .sessionAttr("orders", new Orders())
//        )
//                .andExpect(status().isOk())
//                .andExpect(view().name("api/orders"))
//                .andExpect(model().attribute("orders", hasProperty("customer_id", nullValue())));
//
//        verifyZeroInteractions(mockOrders);
    }
    @Override
    protected void tearDown() throws Exception {
        System.out.println("Running: tearDown");
    }
}

