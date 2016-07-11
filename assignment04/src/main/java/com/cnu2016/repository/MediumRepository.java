package com.cnu2016.repository;

import com.cnu2016.model.Medium;
import com.cnu2016.model.Orders;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by prabh on 09/07/16.
 */
public interface MediumRepository extends CrudRepository<Medium, Integer>{
        public List<Medium> findByOrders(Orders orders);
}

