package com.nishant.drive_clone.s3;

import java.net.URL;
import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
public class service {

	//private final S3Client s3Client;

	private Logger logger;
	
	public service(S3Client s3Client) {
		//this.s3Client = s3Client;
		this.logger = LoggerFactory.getLogger(service.class);
	}
	
	
	
	
    public String createPresignedUrlForUpload(String bucketName, String keyName, String contentType, Map<String, String> metadata) {
        try (S3Presigner presigner = S3Presigner.create()) {

            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType(contentType)
                    .metadata(metadata)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            String uploadUrl = presignedRequest.url().toString();
            logger.info("Presigned URL to upload a file to: [{}]", uploadUrl);
            //logger.info("Which HTTP method needs to be used when uploading a file: [{}]", presignedRequest.httpRequest().method());

            return uploadUrl;
        }
    }
        public String createPresignedUrlForDownload(String bucketName, String keyName, String contentType, Map<String, String> metadata) {
            try (S3Presigner presigner = S3Presigner.create()) {

                GetObjectRequest objectRequest = GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(keyName)
                        .build();

                GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(10))  // The URL will expire in 10 minutes.
                        .getObjectRequest(objectRequest)
                        .build();

                PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);
                String downloadUrl = presignedRequest.url().toString();
                logger.info("Presigned URL to download a file to: [{}]", downloadUrl);
                //logger.info("Which HTTP method needs to be used when uploading a file: [{}]", presignedRequest.httpRequest().method());

                return downloadUrl;
            }
    }
}
