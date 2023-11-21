package com.nishant.drive_clone.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nishant.drive_clone.enums.FileAccessType;
import com.nishant.drive_clone.models.FileEntity;
import com.nishant.drive_clone.models.UserEntity;
import com.nishant.drive_clone.repositories.UserRepository;
import com.nishant.drive_clone.s3.buckets;
import com.nishant.drive_clone.s3.service;

@Service
public class FileService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private buckets bucket;

	@Autowired
	private service s3Service;

	public String upload(String owner, String fileName, Long fileSize, String fileType) {
		FileEntity file = new FileEntity();
		file.setCreationDate(LocalDate.now());
		file.setFileName(fileName);
		file.setFileType(fileType);
		file.setFileSize(fileSize);
		String key = owner + "/" + fileName;
		file.setFileKey(key);
		file.setAccessType(FileAccessType.PRIVATE);

		UserEntity user = userRepository.findByEmail(owner);
		file.setOwner(user);

		List<FileEntity> filesOwned = user.getFiles();
		filesOwned.add(file);
		userRepository.save(user);

		Map<String, String> metaData = new HashMap<>();
		metaData.put("Owner", owner);

		String presignedUrl = s3Service.createPresignedUrlForUpload(bucket.getDrive_dev(), key, fileType, metaData);
		return presignedUrl;

	}

	public String download(String owner, String fileName) {
		String key = owner + "/" + fileName;
		String presignedUrl = s3Service.createPresignedUrlForDownload(bucket.getDrive_dev(), key);
		return presignedUrl;

	}

	public void delete(String owner, String fileName) {
		String key = owner + "/" + fileName;
		s3Service.deleteFile(bucket.getDrive_dev(), key);
	}

}
