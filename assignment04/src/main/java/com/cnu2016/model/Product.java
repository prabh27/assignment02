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
    private Integer productId;

    @Column(name="product_code", columnDefinition = "varchar(255)")
    private String productCode;
    @Column(name="description", columnDefinition="TEXT")
    private String productDescription;

    @Column(name="product_name")
    private String productName;

    @Column(name="buy_price")
    private Double buyPrice;

    @Column(name="sell_price")
    private Double sellPrice;

    @Column(name="quantity")
    private Double quantity;

    @Column(name="is_available")
    private Integer isAvailable = 1;


    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
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

    public Double getQuantity() {
        return quantity;
    }

    public Product() {

    }


    public Integer getProductId() {
        return productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }




}
