package com.cnu2016.repository;

import com.cnu2016.model.Feedback;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


/**
 * Created by prabh on 09/07/16.
 */
public interface FeedbackRepository extends CrudRepository<Feedback, Integer> {

}
