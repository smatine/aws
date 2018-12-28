package com.ats.aws.services;

public interface S3Services {
	public void downloadFile(String keyName);
	public void uploadFile(String keyName, String uploadFilePath);
}
