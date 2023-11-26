package com.nishant.drive_clone.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileViewResponseDto {

	private String fileName;
	private String fileType;
	private Long fileSize;
	private String fileKey;
	private String owner;
	private LocalDate creationDate;
}
