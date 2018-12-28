package com.ats.aws.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.ats.aws.services.SqsServices;
import com.ats.aws.util.Utility;

@Service
public class SqsServicesImpl implements SqsServices {
	
	private Logger logger = LoggerFactory.getLogger(SqsServicesImpl.class);
	
	@Autowired
	private AmazonSQS sqs;

	

	@Override
	public void sendMessage(String queueName) {
		
		try {
			
            System.out.println("Send Message" );
            
            String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();

            SendMessageRequest send_msg_request = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody("hello world")
                    .withDelaySeconds(5);
            sqs.sendMessage(send_msg_request);


            // Send multiple messages to the queue
            SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
                    .withQueueUrl(queueUrl)
                    .withEntries(
                            new SendMessageBatchRequestEntry(
                                    "msg_1", "Hello from message 1"),
                            new SendMessageBatchRequestEntry(
                                    "msg_2", "Hello from message 2")
                                    .withDelaySeconds(10));
            sqs.sendMessageBatch(send_batch_request);
            logger.info("===================== Import File - Done! =====================");
            
        } catch (AmazonServiceException ase) {
        	logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
        	logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        } /*catch (AmazonSQSException  ioe) {
        	logger.info("IOE Error Message: " + ioe.getMessage());
		}*/
	}

	@Override
	public void readMessage(String queueName) {
		
		try {
			String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
			// receive messages from the queue
	        List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

	        // delete messages from the queue
	        for (Message m : messages) {
	        	sqs.deleteMessage(queueUrl, m.getReceiptHandle());
	        }
	        logger.info("===================== Upload File - Done! =====================");
	        
		} catch (AmazonServiceException ase) {
			logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        }
	}

}
