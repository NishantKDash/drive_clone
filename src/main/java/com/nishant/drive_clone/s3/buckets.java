package com.nishant.drive_clone.s3;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix="aws.s3.buckets")
@Getter
@Setter
public class buckets {

	private String drive_dev;
}
