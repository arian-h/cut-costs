package com.boot.cut_costs.utils.AWS;

import java.io.File;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Component
public class S3Services {

	private static Logger logger = LoggerFactory.getLogger(S3Services.class);

	@Autowired
	private AmazonS3 s3client;

	public String uploadFile(FILE_TYPE fileType, File file) {
		if (file == null) {
			return null;
		}
		String keyName = null;
		try {
			keyName = RandomStringUtils.randomAlphanumeric(15);
			s3client.putObject(new PutObjectRequest(fileType.getAwsBucketName(), keyName, file));
		} catch (AmazonServiceException ase) {
			logger.error("Caught AmazonServiceException from PUT requests, rejected reasons: ");
			logger.error("Error Message: " + ase.getMessage());
			logger.error("HTTP status code: " + ase.getStatusCode());
			logger.error("AWS Error Code: " + ase.getErrorCode());
			logger.error("Error Type: " + ase.getErrorType());
			logger.error("File Type: " + fileType);
			keyName= null;
		} catch (AmazonClientException ace) {
			logger.error("Caught an AmazonClientException: ");
			logger.error("Error Message: " + ace.getMessage());
			logger.error("File Type: " + fileType);
			keyName = null;
		}
		logger.debug(String.format("File with type %s and key name %s was successfully uploaded to AWS", fileType, keyName) );
		return keyName;
	}

	public void deleteFile(FILE_TYPE fileType, String keyName) {
		if (keyName != null) {
			try {
				s3client.deleteObject(fileType.getAwsBucketName(), keyName);
			} catch (AmazonServiceException ase) {
				logger.error("Caught AmazonServiceException from DELETE requests, rejected reasons: ");
				logger.error("Error Message: " + ase.getMessage());
				logger.error("HTTP status code: " + ase.getStatusCode());
				logger.error("AWS Error Code: " + ase.getErrorCode());
				logger.error("Error Type: " + ase.getErrorType());
				logger.error("Error Type: " + ase.getErrorType());
				logger.error("File Type: " + fileType);
				logger.error("File key name: " + keyName);
				return;
			}
			logger.debug(String.format("File with type %s and key name %s was successfully uploaded to AWS", fileType, keyName) );
		}
	}
}
