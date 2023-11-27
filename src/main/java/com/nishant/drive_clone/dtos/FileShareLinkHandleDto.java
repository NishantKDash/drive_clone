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
public class FileShareLinkHandleDto {

	String fileName;
	String owner;
	LocalDate creationDate;
	String fileType;
}
