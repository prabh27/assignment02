package com.cnu2016.controller;

import com.cnu2016.model.Log;
import com.cnu2016.queue.AwsQueueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

/**
 * Created by prabh on 11/07/16.
 */
@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class.getName());

    static {
        BasicConfigurator.configure();
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        logger.info("Before handling the request");
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        logger.info("After handling the request");
        // Send a message
        ObjectMapper mapper = new ObjectMapper();
        Log log = new Log();
        String url = request.getRequestURL().toString() + "?" + request.getQueryString();
        log.setUrl(url);
        Integer responseCode = response.getStatus();
        String ipAddress = request.getRemoteAddr();
        long startTime = (Long)request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long executeTime = endTime - startTime;
        log.setDate(startTime);
        log.setExecuteTime(executeTime);
        log.setIpAddress(ipAddress);
        log.setResponseCode(responseCode);

        String jsonInString;

        jsonInString = mapper.writeValueAsString(log);
        super.postHandle(request, response, handler, modelAndView);
        AwsQueueService awsQueueService = new AwsQueueService();
        awsQueueService.sendMessage(jsonInString);
    }


}
