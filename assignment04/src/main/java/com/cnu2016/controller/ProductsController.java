package com.cnu2016.controller;
import com.cnu2016.model.ProductSerializer;
import com.cnu2016.repository.ProductRepository;
import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@RestController
public class ProductsController {

    @Autowired
    private ProductRepository repository;

    @RequestMapping(value="/api/products")
    public ResponseEntity getAll() {
        Iterable p = repository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(p);
    }

   @RequestMapping(value="/api/products/{id}", method=RequestMethod.GET)
    public ResponseEntity getOne(@PathVariable int id) {
        try {
            ProductSerializer p = repository.findOne(id);
            if (p == null || p.getIsAvailable() == false) {
                Map<String, String> detailObject = new HashMap<String, String>();
                detailObject.put("detail", "Not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(p);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/api/products", method=RequestMethod.POST)
    public ResponseEntity postProduct(@RequestBody @Valid ProductSerializer productSerializer) {
        String code = productSerializer.getCode();
        if(code == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Code field empty");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        } else {
            ProductSerializer p = repository.save(productSerializer);
            return ResponseEntity.status(HttpStatus.CREATED).body(p);
        }
    }

    @RequestMapping(value="/api/products/{pk}", method=RequestMethod.PUT)
    public ResponseEntity putProduct(@PathVariable int pk, @RequestBody ProductSerializer productSerializer) {
        ProductSerializer p = repository.findOne(pk);
        if(p == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        } else if(productSerializer.getCode() == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Code field empty");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        } else {
            p.setBuy_price(productSerializer.getBuy_price());
            p.setCode(productSerializer.getCode());
            p.setDescription(productSerializer.getDescription());
            p.setQuantity(productSerializer.getQuantity());
            p.setProduct_name(productSerializer.getProduct_name());
            p.setSell_price(productSerializer.getSell_price());
            ProductSerializer productSerializer1 = repository.save(p);
            return ResponseEntity.status(HttpStatus.CREATED).body(productSerializer1);
        }
    }

    @RequestMapping(value="/api/products/{pk}", method=RequestMethod.PATCH)
    public ResponseEntity patchProduct(@PathVariable int pk, @RequestBody ProductSerializer productSerializer) {
        ProductSerializer p = repository.findOne(pk);
        if(p == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        } else {
            String code = productSerializer.getCode();
            String description = productSerializer.getDescription();
            if(code != null) {
                p.setCode(code);
            }
            if(description != null) {
                p.setDescription(description);
            }
            ProductSerializer productSerializer1 = repository.save(p);
            return ResponseEntity.status(HttpStatus.CREATED).body(productSerializer1);

        }
    }

    @RequestMapping(value="/api/products/{pk}", method=RequestMethod.DELETE)
    public ResponseEntity deleteProduct(@PathVariable int pk) {
        ProductSerializer p = repository.findOne(pk);
        if(p == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        } else {
            p.setIs_available(true);
            ProductSerializer productSerializer1 = repository.save(p);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        }
    }
}
