package com.cnu2016.controller;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.cnu2016.Application;
import com.cnu2016.model.Product;
import com.cnu2016.repository.ProductRepository;
import com.jayway.restassured.RestAssured;
import org.apache.http.HttpStatus;
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
 * Created by prabh on 14/07/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@TestPropertySource("classpath:test.properties")
public class ProductsControllerTest {

    @Autowired
    ProductRepository productRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsControllerTest.class);

    private Product product1;
    private static final int invalidId = -1;

    public ProductsControllerTest() {


    }

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() throws Exception{
        LOGGER.debug("Setting up before every test case");
        RestAssured.port = port;
        product1 = new Product();
        product1.setCode("test1_code");
        product1.setDescription("test1_description");
        productRepository.save(product1);


    }


    @Test
    public void getAll() {                        // get all the products
        RestAssured.when().
                get("/api/products").
        then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void getOne() {                   // get a single product
        int product1Id = product1.getId();
        RestAssured.when().
                get("/api/products/{id}", product1Id).
        then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void getOneInvalidProduct() {                   // get a single product -> id doesn't exist.
        RestAssured.when().
                get("/api/products/{id}", invalidId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void deleteProduct() {                            // delete a product -> happy case
        int product1Id = product1.getId();
        Map<String, String> input = new HashMap<>();
        RestAssured.when().
                delete("/api/products/{pk}", product1Id).
                then().
                statusCode(HttpStatus.SC_OK);

        RestAssured.when().                                 // delete a product -> already deleted.
                delete("/api/products/{pk}", product1Id).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

        RestAssured.when().                                 // get a product -> already deleted.
                get("/api/products/{id}", product1Id).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

        RestAssured.given().                                 // put a product -> already deleted.
                contentType("application/json").
                body(input).
                when().
                put("api/products/{pk}", product1Id).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);

        RestAssured.given().                                 // put a product -> already deleted.
                contentType("application/json").
                body(input).
                when().
                patch("api/products/{pk}", product1Id).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);


    }



    @Test
    public void deleteInvalidProduct() {                       // delete an invalid product
        RestAssured.when().
                delete("/api/products/{pk}", invalidId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }


    @Test
    public void postProduct() {                                 // post an order -> happy case
        Map<String, String> input = new HashMap<>();
        input.put("code", product1.getCode());
        RestAssured.given().
                contentType("application/json").
                body(input).
                when().
                post("api/products").
                then().
                statusCode(HttpStatus.SC_CREATED);
    }

    @Test
    public void postOrderCodeNull() {                           // post an order -> code not given.
        Map<String, String> input = new HashMap<>();
        RestAssured.given().
                contentType("application/json").
                body(input).
                when().
                post("api/products").
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void putOrder() {
        Map<String, String> input = new HashMap<>();
        int product1Id = product1.getId();
        input.put("code", product1.getCode());
        RestAssured.given().                                 // put a product -> happy case.
                contentType("application/json").
                body(input).
                when().
                put("api/products/{pk}", product1Id).
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void putInvalidOrder() {
        Map<String, String> input = new HashMap<>();
        RestAssured.given().                                 // put a product -> invalid product.
                contentType("application/json").
                body(input).
                when().
                put("api/products/{pk}", invalidId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void putOrderCodeNull() {
        Map<String, String> input = new HashMap<>();
        int product1Id = product1.getId();
        RestAssured.given().                                 // put a product -> code null
                contentType("application/json").
                body(input).
                when().
                put("api/products/{pk}", product1Id).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void patchOrder() {
        Map<String, String> input = new HashMap<>();
        int product1Id = product1.getId();
        input.put("code", product1.getCode());
        input.put("description", product1.getDescription());
        RestAssured.given().                                 // patch an product -> happy case.
                contentType("application/json").
                body(input).
                when().
                patch("api/products/{pk}", product1Id).
                then().
                statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void patchInvalidOrder() {
        Map<String, String> input = new HashMap<>();
        RestAssured.given().                                 // patch an product -> invalid product.
                contentType("application/json").
                body(input).
                when().
                patch("api/products/{pk}", invalidId).
                then().
                statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    public void patchOrderCodeNull() {
        Map<String, String> input = new HashMap<>();
        int product1Id = product1.getId();
        RestAssured.given().                                 // patch an product -> code null, description null
                contentType("application/json").
                body(input).
                when().
                patch("api/products/{pk}", product1Id).
                then().
                statusCode(HttpStatus.SC_OK);
    }



    @After
    public void tearDown() {
        productRepository.delete(product1);

    }

}
