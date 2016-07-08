package com.cnu2016.repository;

import com.cnu2016.model.ProductSerializer;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<ProductSerializer, Integer> {

}