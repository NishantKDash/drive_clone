package com.nishant.drive_clone.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nishant.drive_clone.enums.FileAccessType;
import com.nishant.drive_clone.models.AccessEntity;
import com.nishant.drive_clone.models.FileEntity;
import com.nishant.drive_clone.models.UserEntity;
import com.nishant.drive_clone.repositories.AccessRepository;
import com.nishant.drive_clone.repositories.FileRepository;
import com.nishant.drive_clone.repositories.UserRepository;
import com.nishant.drive_clone.s3.buckets;
import com.nishant.drive_clone.s3.service;

@Service
public class FileService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccessRepository accessRepository;

	@Autowired
	private FileRepository fileRepository;

	@Autowired
	private buckets bucket;

	@Autowired
	private service s3Service;

	@Autowired
	private EmailNotificationService emailNotificationService;

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

	public List<FileEntity> getOwnedFiles(String email) {
		UserEntity user = userRepository.findByEmail(email);
		List<FileEntity> ownedFiles = user.getFiles();

		return ownedFiles;
	}

	public List<FileEntity> getAcquiredFiles(String email) {
		UserEntity user = userRepository.findByEmail(email);
		List<AccessEntity> accesses = user.getAccesses();

		List<FileEntity> acquiredFiles = accesses.stream().map((access) -> {
			return access.getFile();
		}).collect(Collectors.toList());

		return acquiredFiles;
	}

	public void addAccesses(String owner, String fileName, List<String> users) {
		UserEntity ownerUser = userRepository.findByEmail(owner);
		List<FileEntity> ownedFiles = ownerUser.getFiles();
		FileEntity selectedFile = ownedFiles.stream().filter((ownedFile) -> {
			return ownedFile.getFileName().equals(fileName);
		}).findFirst().get();

		users.stream().forEach((user) -> {
			UserEntity consumer = userRepository.findByEmail(user);
			if (accessRepository.accessByOwnerConsumerFile(ownerUser, consumer, selectedFile) == null) {
				AccessEntity newAccess = new AccessEntity();
				newAccess.setConsumer(consumer);
				newAccess.setOwner(ownerUser);
				newAccess.setFile(selectedFile);

				List<AccessEntity> accesses = consumer.getAccesses();
				accesses.add(newAccess);
				List<AccessEntity> allAccesses = selectedFile.getAccesses();
				allAccesses.add(newAccess);

				userRepository.save(consumer);
				fileRepository.save(selectedFile);

				emailNotificationService.sendEmail(consumer.getEmail(), "Access Granted",
						"Hi, You have been granted access to " + selectedFile.getFileName() + "by "
								+ ownerUser.getEmail());
			}
		});

	}

	public void removeAccesses(String owner, String fileName, List<String> users) {

		UserEntity ownerUser = userRepository.findByEmail(owner);
		List<FileEntity> ownedFiles = ownerUser.getFiles();
		FileEntity selectedFile = ownedFiles.stream().filter((ownedFile) -> {
			return ownedFile.getFileName().equals(fileName);
		}).findFirst().get();

		users.stream().forEach((user) -> {
			UserEntity consumer = userRepository.findByEmail(user);
			AccessEntity access = accessRepository.accessByOwnerConsumerFile(ownerUser, consumer, selectedFile);

			List<AccessEntity> acquiredAccesses = consumer.getAccesses();
			List<AccessEntity> listedAccesses = selectedFile.getAccesses();

			acquiredAccesses = acquiredAccesses.stream().filter((a) -> {
				return a.getId() != access.getId();
			}).collect(Collectors.toList());

			listedAccesses = listedAccesses.stream().filter((a) -> {
				return a.getId() != access.getId();
			}).collect(Collectors.toList());

			consumer.setAccesses(acquiredAccesses);
			selectedFile.setAccesses(listedAccesses);

			accessRepository.delete(access);
			userRepository.save(consumer);
			fileRepository.save(selectedFile);
			emailNotificationService.sendEmail(consumer.getEmail(), "Access Removed", "Hi, Your access for this file "
					+ selectedFile.getFileName() + "has been removed by " + ownerUser.getEmail());

		});
	}

}
