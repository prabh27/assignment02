package com.cnu2016.controller;
import com.cnu2016.model.Product;
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
    public ResponseEntity getOne(@PathVariable Integer id) {
        Product p = repository.findOne(id);
        System.out.println(p);
        if (p == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        }
        if(p.getIsAvailable() == 0)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        return ResponseEntity.status(HttpStatus.OK).body(p);

    }

    @RequestMapping(value="/api/products", method=RequestMethod.POST)
    public ResponseEntity postProduct(@RequestBody @Valid Product product) {
        String code = product.getProductCode();
        if(code == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Code field empty");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        }
        Product p = repository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(p);

    }

    @RequestMapping(value="/api/products/{pk}", method=RequestMethod.PUT)
    public ResponseEntity putProduct(@PathVariable Integer pk, @RequestBody Map<String, String> map) {
        Product p = repository.findOne(pk);
        if(p == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        }
        if(p.getIsAvailable() != 1)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        if(map.get("code") == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Code field empty");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        } else {
            p.setProductCode(map.get("code"));
            p.setProductDescription(map.get("description"));
            Product product = repository.save(p);
            return ResponseEntity.status(HttpStatus.OK).body(product);
        }
    }

    @RequestMapping(value="/api/products/{pk}", method=RequestMethod.PATCH)
    public ResponseEntity patchProduct(@PathVariable Integer pk, @RequestBody Product product) {
        Product p = repository.findOne(pk);
        if(p == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        }
        if(p.getIsAvailable() != 1)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
        String code = product.getProductCode();
        String description = product.getProductDescription();
        if(code != null) {
            p.setProductCode(code);
        }
        if(description != null) {
            p.setProductDescription(description);
        }
        Product product1 = repository.save(p);
        return ResponseEntity.status(HttpStatus.OK).body(product1);

    }

    @RequestMapping(value="/api/products/{pk}", method=RequestMethod.DELETE)
    public ResponseEntity deleteProduct(@PathVariable Integer pk) {
        Product p = repository.findOne(pk);
        if(p == null) {
            Map<String, String> detailObject = new HashMap<String, String>();
            detailObject.put("detail", "Not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(detailObject);
        } else {
            p.setIsAvailable(0);
            Product product1 = repository.save(p);
            return ResponseEntity.status(HttpStatus.OK).body("");
        }
    }
}
