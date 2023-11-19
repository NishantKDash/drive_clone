package com.nishant.drive_clone.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nishant.drive_clone.dtos.FileUploadRequestDto;
import com.nishant.drive_clone.dtos.FileUploadResponseDto;

@RestController
@RequestMapping("/api/file")
public class FileController {
  
	
	@GetMapping("/upload")
	public FileUploadResponseDto upload(@RequestBody FileUploadRequestDto fileDto)
	{
		
	}
}
