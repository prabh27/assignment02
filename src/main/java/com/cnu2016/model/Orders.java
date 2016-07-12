package com.cnu2016.model;

import javax.persistence.*;

/**
 * Created by prabh on 08/07/16.
 */

@Entity
@Table(name="Orders")
public class Orders {

    public Orders() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="order_id")
    private Integer orderId;

    @Column(name="order_date")
    private String orderDate;





    @Column(name="status")
    private String status;

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }



    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getOrderId() {

        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }



    public String getStatus() {
        return status;
    }
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    public Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
/*

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    public Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }*/
}
