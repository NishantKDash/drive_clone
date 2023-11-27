package com.nishant.drive_clone.controllers;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nishant.drive_clone.dtos.AccessAddOrRemoveDto;
import com.nishant.drive_clone.dtos.FileDownloadRequestDto;
import com.nishant.drive_clone.dtos.FileDownloadResponseDto;
import com.nishant.drive_clone.dtos.FileShareLinkHandleDto;
import com.nishant.drive_clone.dtos.FileShareLinkResponseDto;
import com.nishant.drive_clone.dtos.FileUploadRequestDto;
import com.nishant.drive_clone.dtos.FileUploadResponseDto;
import com.nishant.drive_clone.dtos.FileViewResponseDto;
import com.nishant.drive_clone.models.FileEntity;
import com.nishant.drive_clone.services.FileService;

@RestController
@RequestMapping("/api/file")
public class FileController {

	@Autowired
	private FileService fileService;

	@Value("${client.domain}")
	private String clientDomain;

	@GetMapping("/upload")
	public FileUploadResponseDto upload(@RequestBody FileUploadRequestDto fileDto, Principal user) {

		FileUploadResponseDto fileResponseDto = new FileUploadResponseDto();
		String presignedUrl = fileService.upload(user.getName(), fileDto.getFileName(), fileDto.getFileSize(),
				fileDto.getFileType());
		fileResponseDto.setPresignedUrl(presignedUrl);

		return fileResponseDto;
	}

	@GetMapping("/download")
	public FileDownloadResponseDto download(@RequestBody FileDownloadRequestDto fileDto) {
		FileDownloadResponseDto fileResponseDto = new FileDownloadResponseDto();

		String presignedUrl = fileService.download(fileDto.getOwner(), fileDto.getFileName());
		fileResponseDto.setPresignedUrl(presignedUrl);
		return fileResponseDto;
	}

	@DeleteMapping("/delete/{fileName}")
	public ResponseEntity<String> delete(@PathVariable String fileName, Principal user) {
		try {
			fileService.delete(user.getName(), fileName);
			return ResponseEntity.ok("The file has been deleted");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/view")
	public ResponseEntity<List<FileViewResponseDto>> getFiles(Principal user) {
		List<FileEntity> ownedFiles = fileService.getOwnedFiles(user.getName());
		List<FileEntity> acquiredFiles = fileService.getAcquiredFiles(user.getName());

		List<FileViewResponseDto> response = ownedFiles.stream().map(file -> {
			FileViewResponseDto fileResponse = new FileViewResponseDto();
			fileResponse.setFileName(file.getFileName());
			fileResponse.setCreationDate(file.getCreationDate());
			fileResponse.setFileKey(file.getFileKey());
			fileResponse.setFileSize(file.getFileSize());
			fileResponse.setFileType(file.getFileType());
			fileResponse.setOwner(file.getOwner().getEmail());
			return fileResponse;
		}).collect(Collectors.toList());

		response.addAll(acquiredFiles.stream().map(file -> {
			FileViewResponseDto fileResponse = new FileViewResponseDto();
			fileResponse.setFileName(file.getFileName());
			fileResponse.setCreationDate(file.getCreationDate());
			fileResponse.setFileKey(file.getFileKey());
			fileResponse.setFileSize(file.getFileSize());
			fileResponse.setFileType(file.getFileType());
			fileResponse.setOwner(file.getOwner().getEmail());
			return fileResponse;
		}).collect(Collectors.toList()));

		return ResponseEntity.ok(response);
	}

	@PostMapping("/{fileName}/access/add")
	public ResponseEntity<String> addAccesses(@PathVariable String fileName,
			@RequestBody AccessAddOrRemoveDto accessDto, Principal owner) {

		fileService.addAccesses(owner.getName(), fileName, accessDto.getUsers());
		return ResponseEntity.ok("Access have been added");
	}

	@PostMapping("/{fileName}/access/remove")
	public ResponseEntity<String> removeAccesses(@PathVariable String fileName,
			@RequestBody AccessAddOrRemoveDto accessDto, Principal owner) {
		fileService.removeAccesses(owner.getName(), fileName, accessDto.getUsers());
		return ResponseEntity.ok("Access have been removed");
	}

	@GetMapping("/{fileName}/public")
	public ResponseEntity<FileShareLinkResponseDto> makeFilePublic(@PathVariable String fileName, Principal owner) {
		FileShareLinkResponseDto fileShareLinkDto = new FileShareLinkResponseDto();
		fileShareLinkDto.setFileName(fileName);
		fileShareLinkDto.setOwner(owner.getName());
		String link = fileService.createFileShareLink(fileName, owner.getName());
		String sharableLink = clientDomain + "/" + link;
		fileShareLinkDto.setLink(sharableLink);
		return ResponseEntity.ok(fileShareLinkDto);
	}

	@PostMapping("/{fileName}/private")
	public ResponseEntity<String> makeFilePrivate(@PathVariable String fileName, Principal owner) {
		fileService.makeFilePrivate(fileName, owner.getName());
		return ResponseEntity.ok("The file is now private");
	}

	@GetMapping("/share/{id}")
	public ResponseEntity<FileShareLinkHandleDto> handleFileShareLink(@PathVariable String id) {
		FileShareLinkHandleDto fileShareLinkHandleDto = fileService.handleFileShareLink(id);
		return ResponseEntity.ok(fileShareLinkHandleDto);

	}
}
