package com.nishant.drive_clone.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LinkEntity extends BaseModel{

	@ManyToOne
	private FileEntity file;
	
	private String linkCode;
}
