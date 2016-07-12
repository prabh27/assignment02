package com.cnu2016.queue;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by prabh on 11/07/16.
 */
public class AwsQueueService {
    private static AmazonSQS sqs;
    public AwsQueueService() {
        AWSCredentials credentials = null;

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. " +
                            "Please make sure that your credentials file is at the correct " +
                            "location (~/.aws/credentials), and is in valid format.",
                    e);
        }

        AmazonSQS sqs = new AmazonSQSClient(credentials);
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        sqs.setRegion(usEast1);
        this.sqs = sqs;
    }

    public void sendMessage(String s) {
        try {
            String myQueueUrl = sqs.getQueueUrl("cnu2016_prabh_simran_assignment05a").getQueueUrl();
            sqs.sendMessage(new SendMessageRequest(myQueueUrl, s));

        } catch (AmazonServiceException ase) {
            throw new AmazonClientException(
                    ase.getMessage(),
                    ase);

        } catch (AmazonClientException ace) {
            throw new AmazonClientException(
                    ace.getMessage(),
                    ace);
        }
    }
}

