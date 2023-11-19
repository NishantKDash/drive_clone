package com.nishant.drive_clone.services;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nishant.drive_clone.models.FileEntity;
import com.nishant.drive_clone.repositories.FileRepository;
import com.nishant.drive_clone.s3.service;

@Service
public class FileService {

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private service s3Service;

	public String upload(String owner, String fileName, Long fileSize, String fileType) {
		FileEntity file = new FileEntity();
		file.setCreationDate(LocalDate.now());
		file.setFileName(fileName);
		file.setFileType(fileType);
		file.setFileSize(fileSize);

		fileRepository.save(file);

		String presignedUrl = s3Service.createPresignedUrlForUpload(null, null, null, null);
		return presignedUrl;

	}

	public String download(String fileName) {

		String presignedUrl = s3Service.createPresignedUrlForDownload(null, null, null, null);
		return presignedUrl;

	}

}
