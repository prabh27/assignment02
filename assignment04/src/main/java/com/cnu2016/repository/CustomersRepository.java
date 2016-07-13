package com.cnu2016.repository;

import com.cnu2016.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by prabh on 09/07/16.
 */
public interface CustomersRepository extends CrudRepository<Customer, Integer>{
    Customer findByCustomerName(String customerName);
}
