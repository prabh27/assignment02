package com.cnu2016.controller;

import com.cnu2016.model.*;
import com.cnu2016.repository.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Type;
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
    @RequestMapping(value = "/api/orders/{pk}", method = RequestMethod.POST)
    public ResponseEntity getOrder(@RequestBody Map<String, String> inputs, @PathVariable Integer pk) {
       // System.out.println(inputs.get("productId"));
       // System.out.println(inputs.get("quantity"));
        int productId = Integer.parseInt(inputs.get("productId"));
        double quantity = Double.parseDouble(inputs.get("quantity"));
        Orders o = ordersRepository.findOne(pk);
        if(o == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        } else {
            Medium m = new Medium();
            m.setOrders(o);
            Product product = productRepository.findOne(productId);
            if(quantity > product.getQuantity()) {
                Map<String, String> detailObject = new HashMap<String, String>();
                detailObject.put("detail", "Quantity not available");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
            }
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
            if(customer != null)         // if user logged in.
            {
                customer.setAddressLine1(addressLine);
                customersRepository.save(customer);
            }
            List<Medium> m = mediumRepository.findByOrders(o);  // edit all orders.
            for (Medium medium : m) {
                Product p = medium.getProducts();
                System.out.println(p);
                double oldQuantity = p.getQuantity();
                p.setQuantity(oldQuantity - medium.getQuantity());
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

}
