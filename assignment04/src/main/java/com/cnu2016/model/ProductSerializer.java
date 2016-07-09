package com.cnu2016.model;

import javax.persistence.*;

/**
 * Created by prabh on 08/07/16.
 */

@Entity
@Table(name="Products")
public class ProductSerializer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="product_id")
    private Integer id;

    @Column(name="product_code", columnDefinition = "varchar(255)")
    private String code;
    @Column(name="description", columnDefinition="TEXT")
    private String description;

    private String product_name;
    private Double buy_price;
    private Double sell_price;
    private Double quantity;
    private Boolean is_available = true;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIs_available(Boolean is_available) {
        this.is_available = is_available;
    }

    public Boolean getIs_available() {

        return is_available;
    }

    public void setProduct_name(String product_name) {
        if(product_name == null) {
            this.product_name = "";
        } else {
            this.product_name = product_name;
        }
    }

    public void setBuy_price(Double buy_price) {
        this.buy_price = buy_price;
    }

    public void setSell_price(Double sell_price) {
        this.sell_price = sell_price;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getProduct_name() {
        return product_name;
    }

    public Double getBuy_price() {
        return buy_price;
    }

    public Double getSell_price() {
        return sell_price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public ProductSerializer() {

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
