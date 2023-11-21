package com.nishant.drive_clone.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nishant.drive_clone.dtos.FileDownloadRequestDto;
import com.nishant.drive_clone.dtos.FileDownloadResponseDto;
import com.nishant.drive_clone.dtos.FileUploadRequestDto;
import com.nishant.drive_clone.dtos.FileUploadResponseDto;
import com.nishant.drive_clone.services.FileService;

@RestController
@RequestMapping("/api/file")
public class FileController {

	@Autowired
	private FileService fileService;

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
}
