package com.cnu2016.model;

import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import javax.persistence.criteria.Order;
import java.util.List;

/**
 * Created by prabh on 08/07/16.
 */

@Entity
@Table(name="Medium")
public class Medium {

    public Medium() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="medium_id")
    private Integer mediumId;



    public void setMediumId(Integer mediumId) {
        this.mediumId = mediumId;
    }

    public Integer getMediumId() {

        return mediumId;
    }

    @ManyToOne
    @JoinColumn(name="order_id", referencedColumnName = "order_id")
    private Orders orders;

    public void setOrders(Orders orders) {
        this.orders = orders;
    }


    public Orders getOrders() {

        return orders;
    }


    public void setProducts(Product products) {
        this.products = products;
    }

    public Product getProducts() {

        return products;
    }

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "product_id")
    private Product products;

    @Column(name="quantity")
    private Double quantity;

    @Column(name="price")
    private Double price;



    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


    public Double getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }



}
