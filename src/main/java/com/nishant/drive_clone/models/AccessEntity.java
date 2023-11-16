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
public class AccessEntity extends BaseModel {

	@ManyToOne
	private UserEntity owner;

	@ManyToOne
	private UserEntity consumer;

	@ManyToOne
	private FileEntity file;
}
