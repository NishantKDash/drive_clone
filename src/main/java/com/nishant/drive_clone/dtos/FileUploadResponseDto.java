package com.nishant.drive_clone.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileUploadResponseDto {
 
	
	private String presignedUrl;
}
