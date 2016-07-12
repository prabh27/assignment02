package com.cnu2016.controller;

import com.cnu2016.model.*;
import com.cnu2016.repository.*;
import org.aspectj.weaver.ast.Or;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.Order;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by prabh on 09/07/16.
 */

@RestController
public class OrdersController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class.getName());
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private MediumRepository mediumRepository;

    @Autowired
    private CustomersRepository customersRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private FeedbackRepository feedbackRepository;

//    @RequestMapping(value = "/api/orders", method = RequestMethod.GET)
//    public ResponseEntity getAll() {
//        Iterable orders = ordersRepository.findAll();
//        if(orders == null)
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NULL");
//        else
//            return ResponseEntity.status(HttpStatus.OK).body(orders);
//    }

    @RequestMapping(value = "/api/health", method = RequestMethod.GET)
    public ResponseEntity health() {
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @RequestMapping(value="/api/orders/{id}", method=RequestMethod.GET)
    public ResponseEntity getOne(@PathVariable Integer id) {
        Orders o = ordersRepository.findOne(id);
        if (o == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        }
        if(o.getIsAvailable() == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        return ResponseEntity.status(HttpStatus.OK).body("");

    }

    @RequestMapping(value = "/api/orders", method = RequestMethod.POST)
    public ResponseEntity postOrder() {
        Orders orders = new Orders();
        ordersRepository.save(orders);
        return ResponseEntity.status(HttpStatus.CREATED).body(orders);

    }

    /* List an Item in an order
        Find the order
            if not found 404 Not Found
        if product quantity is greater than available, exception.
        Add a row in the Medium table
        Set status of Order = SHopping Cart
     */
    @RequestMapping(value = "/api/orders/{pk}/orderLineItem", method = RequestMethod.POST)
    public ResponseEntity getOrder(@RequestBody Map<String, String> inputs, @PathVariable Integer pk) {
        if(inputs.get("product_id") == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        if(inputs.get("quantity") == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        int productId = Integer.parseInt(inputs.get("product_id"));
        double quantity = Double.parseDouble(inputs.get("quantity"));
        System.out.println(productId);
        System.out.println(quantity);
        Orders o = ordersRepository.findOne(pk);

      //  if(o != null && o.getIsAvailable() == )
        //    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        if(o == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        } else {
            Medium m = new Medium();
            if(o.getIsAvailable() == 0)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
            m.setOrders(o);
            Product product = productRepository.findOne(productId);
            if(product == null)
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
            m.setProducts(product);
            m.setPrice(product.getSellPrice());
            m.setQuantity(quantity);
            mediumRepository.save(m);
            o.setStatus("Shopping Cart");
            ordersRepository.save(o);
            return ResponseEntity.status(HttpStatus.CREATED).body(m);
        }


    }

    /* Submit an Order
        if order not found -> 404
        Change status to "Shipping"
        Reduce inventory
     */
    @RequestMapping(value = "/api/orders/{pk}", method = RequestMethod.PATCH)
    public ResponseEntity submitOrder(@RequestBody Map<String, String> inputs, @PathVariable int pk) {
        return ResponseEntity.status(HttpStatus.CREATED).body(""); 
        Orders o = ordersRepository.findOne(pk);
        String addressLine = inputs.get("address");
        if(addressLine == null) {                  // Check if address if given or not.
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("address", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        }

        String customerName = inputs.get("user_name");
        Customer customer = o.getCustomer();
        if(o == null) {                   // Order not found.
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        } else {
            if(customerName != null && customer == null) {     // user_name given
                Customer c = customersRepository.findUniqueByCustomerName(customerName);
                o.setCustomer(c);
                c.setAddressLine1(addressLine);
                customersRepository.save(c);
            }
            if(customer == null)         // if user logged in.
                customer = new Customer();

            customer.setAddressLine1(addressLine);
            customer.setCustomerName(customerName);
            customersRepository.save(customer);
            o.setCustomer(customer);
            List<Medium> m = mediumRepository.findByOrders(o);  // edit all orders.
            for (Medium medium : m) {
                Product p = medium.getProducts();
                System.out.println(p);
                double oldQuantity = p.getQuantity();
                p.setQuantity(oldQuantity - medium.getQuantity());
                if(p.getQuantity() < 0) {
                    Map<String, String> detailObject = new HashMap<String, String>();
                    detailObject.put("detail", "Not found.");
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
                }
                productRepository.save(p);

            }
            o.setStatus("Checkout");
            ordersRepository.save(o);
            return ResponseEntity.status(HttpStatus.CREATED).body(o);
        }
    }


    /*
    Contact Us -> Add fields email id, description.
     */
    @RequestMapping(value = "/api/ContactUs", method = RequestMethod.POST)
    public ResponseEntity postContactUs(@RequestBody Map<String, String >map) {
        Feedback feedback = new Feedback();
        if(map.get("customer_id") != null) {
            Customer customer = customersRepository.findOne(Integer.parseInt(map.get("customer_id")));
            feedback.setCustomer(customer);
        }
        feedback.setDescription(map.get("description"));
        feedback.setEmailAddress(map.get("email_address"));
        Feedback f = feedbackRepository.save(feedback);
        return ResponseEntity.status(HttpStatus.CREATED).body(f);
    }

    @RequestMapping(value="/api/orders/{pk}", method=RequestMethod.DELETE)
    public ResponseEntity deleteProduct(@PathVariable Integer pk) {
        Orders orders = ordersRepository.findOne(pk);
        if(orders == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        } else {
            orders.setIsAvailable(0);
            Orders orders1 = ordersRepository.save(orders);
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
    }

}
