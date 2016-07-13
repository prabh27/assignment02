package com.cnu2016.model;

import javax.persistence.*;

/**
 * Created by prabh on 08/07/16.
 */

@Entity
@Table(name="Products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="product_id")
    private Integer id;

    @Column(name="product_code", columnDefinition = "varchar(255)")
    private String code;

    @Column(name="description", columnDefinition="TEXT")
    private String description;

    @Column(name="product_name")
    private String productName;

    @Column(name="buy_price")
    private Double buyPrice;

    @Column(name="sell_price")
    private Double sellPrice;

    @Column(name="quantity")
    private Double qty;

    @Column(name="is_available")
    private Integer isAvailable = 1;


    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Integer isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setProductName(String productName) {
        if(productName == null) {
            this.productName = "";
        } else {
            this.productName = productName;
        }
    }

    public void setBuyPrice(Double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public void setSellPrice(Double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public String getProductName() {
        return productName;
    }

    public Double getBuyPrice() {
        return buyPrice;
    }

    public Double getSellPrice() {
        return sellPrice;
    }

    public Double getQty() {
        return qty;
    }

    public Product() {

    }


    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }




}
