package com.cnu2016.controller;


import com.cnu2016.Application;
import com.cnu2016.model.*;
import com.cnu2016.repository.*;
import com.jayway.restassured.RestAssured;
import junit.framework.TestCase;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by prabh on 12/07/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@TestPropertySource("classpath:test.properties")
public class OrdersControllerTest extends TestCase {

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CustomersRepository customersRepository;

    @Autowired
    MediumRepository mediumRepository;


    @Value("${local.server.port}")
    int port;

    private Orders orders1;
    private Orders orders2;
    private Orders orders3;
    private Product product1;
    private Customer customer1;
    private Medium medium1;
    private String address;
    private static final int invalidId = -1;
    private static final String invalidCustomerName = "invalidName";

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsControllerTest.class);


    @Before
    public void setUp() throws Exception {
        RestAssured.port = port;
        orders1 = new Orders();
        orders1.setStatus("Order Created");
        orders2 = new Orders();
        orders2.setStatus("Order Created");
        orders3 = new Orders();
        orders3.setStatus("Order Created");
        ordersRepository.save(Arrays.asList(orders1, orders2, orders3));

        product1 = new Product();
        product1.setIsAvailable(1);
        product1.setBuyPrice(100.0);
        product1.setSellPrice(120.0);
        product1.setQty(100.0);
        product1.setCode("abcd");
        product1.setProductName("test_product");
        productRepository.save(product1);

        medium1 = new Medium();
        medium1.setProducts(product1);
        medium1.setOrders(orders1);
        medium1.setQuantity(50.0);
        mediumRepository.save(medium1);

        customer1 = new Customer();
        customer1.setCustomerName("test_name");
        address = "test_address";


    }

    @Test                                           // test health.
    public void testHealth() {
        RestAssured.
                when().
                get("/api/health").
                then().
                statusCode(HttpStatus.SC_OK);
    }


    @Test
    public void deleteOrder() {                       // delete an order -> happy case
        int order2Id = orders2.getId();
        Map<String, Integer> input = new HashMap<>();
        int product1Id = product1.getId();
        input.put("product_id", product1Id);
        Double qty = product1.getQty();
        input.put("qty", qty.intValue());
        RestAssured.when().
                delete("/api/orders/{pk}", order2Id).
                then().
                statusCode(HttpStatus.SC_OK);

        RestAssured.given().                                // submit deleted product.
                contentType("application/json").
                body(input).
                when().
                patch("/api/orders/{pk}", order2Id).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

        RestAssured.when().
                delete("/api/orders/{pk}", order2Id).       // delete already deleted order.
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);


        RestAssured.given().                               // post a deleted order.
                contentType("application/json").
                body(input).
                when().
                post("/api/orders/{pk}/orderLineItem", order2Id).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);


        RestAssured.when().                                 // get deleted order.
                get("/api/orders/{order_id}", order2Id).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void deleteInvalidOrder() {                       // delete an order -> Order does not exist.
        RestAssured.when().
                delete("/api/orders/{pk}", invalidId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }


    @Test
    public void getOrder() {                   // check if orderId exists or not.
        int orders1Id = orders1.getId();
        RestAssured.when().
                get("/api/orders/{id}", orders1Id).
                then().
                statusCode(HttpStatus.SC_OK).
                body("status", Matchers.is("Order Created"));

    }

    @Test
    public void getInvalidOrder() {                   // get invalid order.
        RestAssured.when().
                get("/api/orders/{id}", invalidId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }


    @Test
    public void postOrder() {                 // check if order created or not.
        RestAssured.when().
                post("/api/orders").
                then().
                statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void addItemtoOrder() {               // check -> Add an item to an order-> Happy test case
        Map<String, Integer> product = new HashMap<>();
        int orders1Id = orders1.getId();
        int product1Id = product1.getId();
        product.put("product_id", product1Id);
        Double qty = product1.getQty();
        product.put("qty", qty.intValue());
        RestAssured.given().
                contentType("application/json").
                body(product).
                when().
                post("/api/orders/{pk}/orderLineItem", orders1Id).
                then().
                statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void addItemtoInvalidOrder() {               // check -> Add an item to an order-> Invalid order id.
        Map<String, Integer> product = new HashMap<>();
        int product1Id = product1.getId();
        product.put("product_id", product1Id);
        Double qty = product1.getQty();
        product.put("qty", qty.intValue());
        RestAssured.given().
                contentType("application/json").
                body(product).
                when().
                post("/api/orders/{pk}/orderLineItem", invalidId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void addItemToOrderWithoutProductId() {    // check -> Add an item -> product_id not given.
        Map<String, Integer> product = new HashMap<>();
        int orders1Id = orders1.getId();
        Double qty = product1.getQty();
        product.put("qty", qty.intValue());
        RestAssured.given().
                contentType("application/json").
                body(product).
                when().
                post("/api/orders/{pk}/orderLineItem", orders1Id).
                then().
                statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void addItemToOrderWithoutProdutQty() {    // check -> Add an item -> qty not given.
        Map<String, Integer> product = new HashMap<>();
        int orders1Id = orders1.getId();
        int product1Id = product1.getId();
        product.put("product_id", product1Id);
        RestAssured.given().
                contentType("application/json").
                body(product).
                when().
                post("/api/orders/{pk}/orderLineItem", orders1Id).
                then().
                statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void submitOrder() {                // Check if order submitted or not -> Happy case
        Map<String, String> input = new HashMap<>();
        int orders1Id = orders1.getId();
        input.put("user_name", customer1.getCustomerName());
        input.put("address", address);
        RestAssured.given().
                contentType("application/json").
                body(input).
                when().
                patch("/api/orders/{pk}", orders1Id).
                then().
                statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void submitOrderWithoutUserName() {                // Check if order submitted or not -> Username not given
        Map<String, String> input = new HashMap<>();
        int orders1Id = orders1.getId();
        input.put("address", address);
        RestAssured.given().
                contentType("application/json").
                body(input).
                when().
                patch("/api/orders/{pk}", orders1Id).
                then().
                statusCode(HttpStatus.SC_OK);

    }

    @Test
    public void submitOrderWithoutAddress() {                // Check if order submitted or not -> Address not given
        Map<String, String> input = new HashMap<>();
        int orders1Id = orders1.getId();
        input.put("user_name", customer1.getCustomerName());
        RestAssured.given().
                contentType("application/json").
                body(input).
                when().
                patch("/api/orders/{pk}", orders1Id).
                then().
                statusCode(HttpStatus.SC_BAD_REQUEST);

    }

    @Test
    public void submitOrderWithInvalidCustomerName() {         // Check if order submitted or not -> Username invalid
        Map<String, String> input = new HashMap<>();
        int orders1Id = orders1.getId();
        input.put("address", address);
        input.put("user_name", invalidCustomerName);
        LOGGER.debug(invalidCustomerName);
        RestAssured.given().
                contentType("application/json").
                body(input).
                when().
                patch("/api/orders/{pk}", orders1Id).
                then().
                statusCode(HttpStatus.SC_OK);

    }


    @Test
    public void submitOrderWithInvalidOrderid() {                // Check if order submitted or not ->  order does not exists
        Map<String, String> input = new HashMap<>();
        input.put("user_name", customer1.getCustomerName());
        RestAssured.given().
                contentType("application/json").
                body(input).
                when().
                patch("/api/orders/{pk}", invalidId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

    }

    @Test
    public void ContactUs() {
        Map<String, String> input = new HashMap<>();
        input.put("description", "test_description");
        input.put("email_address", "test_email_address");
        RestAssured.given().
                contentType("application/json").
                body(input).
                when().
                post("/api/ContactUs").
                then().
                statusCode(HttpStatus.SC_CREATED);
    }

}

