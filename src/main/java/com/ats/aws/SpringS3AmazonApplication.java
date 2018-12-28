package com.ats.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.ats.aws.services.S3Services;
import com.ats.aws.services.SqsServices;

@SpringBootApplication
public class SpringS3AmazonApplication implements CommandLineRunner{

	
	@Autowired
	S3Services s3Services;
	
	@Autowired
	SqsServices sqsServices;
	
	@Value("${jsa.s3.uploadfile}")
	private String uploadFilePath;
	
	@Value("${jsa.s3.key}")
	private String downloadKey;
	
	@Value("${jsa.sqs.queue}")
	private String queueName;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringS3AmazonApplication.class, args);
	}

	@Value("${jsa.aws.access_key_id}")
	private String awsId;

	@Value("${jsa.aws.secret_access_key}")
	private String awsKey;
	
	@Value("${jsa.s3.region}")
	private String region;
	
	@Override
	public void run(String... args) throws Exception {
		/*System.out.println("---------------- START UPLOAD FILE ----------------");
		s3Services.uploadFile("jsa-s3-upload-file2.txt", uploadFilePath);
		System.out.println("---------------- START DOWNLOAD FILE ----------------");
		s3Services.downloadFile(downloadKey);*/
		
		/*System.out.println("---------------- START SEND MESSAGE ----------------");
		sqsServices.sendMessage(queueName);
		System.out.println("---------------- START READ MESSAGE ----------------");
		sqsServices.readMessage(queueName);*/
		
		
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
		int timeoutConnection = 30000;
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        /*clientConfiguration.setMaxErrorRetry(2);
        clientConfiguration.setConnectionTimeout(timeoutConnection);
        //clientConfiguration.setSocketTimeout(timeoutConnection);*/
        //clientConfiguration.setProtocol(Protocol.HTTPS);
        
        clientConfiguration.setProxyHost("myproxy");
        clientConfiguration.setProxyPort(8080);
        //clientConfiguration.setNonProxyHosts("sqs.eu-west-1.amazonaws.com");
        
		AmazonSQS sqs = AmazonSQSClientBuilder.standard()
				.withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .withClientConfiguration(clientConfiguration)
                .build();
	
		 String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
		 System.out.println("-- " + queueUrl);

	        SendMessageRequest send_msg_request = new SendMessageRequest()
	                .withQueueUrl(queueUrl)
	                .withMessageBody("hello world")
	                .withDelaySeconds(5);
	        sqs.sendMessage(send_msg_request);
		
	}
}
