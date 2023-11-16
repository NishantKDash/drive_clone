package com.nishant.drive_clone.s3;

import org.springframework.stereotype.Service;

import software.amazon.awssdk.services.s3.S3Client;

@Service
public class service {

	private final S3Client s3Client;

	public service(S3Client s3Client) {
		this.s3Client = s3Client;
	}
}
