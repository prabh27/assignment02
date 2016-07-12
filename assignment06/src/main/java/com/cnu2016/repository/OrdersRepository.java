package com.cnu2016.repository;


import com.cnu2016.model.Medium;
import com.cnu2016.model.Orders;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.criteria.Order;
import java.util.List;


public interface OrdersRepository extends CrudRepository<Orders, Integer>{
    public List<Medium> findByOrderId(Integer id);
}

