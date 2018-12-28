package com.ats.aws.services;

public interface SqsServices {
	public void sendMessage(String queueName);
	public void readMessage(String queueName);
}
