package com.nishant.drive_clone.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadRequestDto {

	
	private String fileName;
	private String fileSize;
	private String fileType;
}
