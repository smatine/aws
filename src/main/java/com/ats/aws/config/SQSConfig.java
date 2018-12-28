package com.ats.aws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;

@Configuration
public class SQSConfig {
	@Value("${jsa.aws.access_key_id}")
	private String awsId;

	@Value("${jsa.aws.secret_access_key}")
	private String awsKey;
	
	@Value("${jsa.s3.region}")
	private String region;

	@Bean
	public AmazonSQS sqs() {
		
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
		AmazonSQS sqs = AmazonSQSClientBuilder.standard()
				.withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
		
		return sqs;
	}
}
