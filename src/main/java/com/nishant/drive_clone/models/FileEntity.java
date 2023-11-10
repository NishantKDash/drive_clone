package com.nishant.drive_clone.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity extends BaseModel {

	@ManyToOne
	private UserEntity owner;

	@OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
	private List<AccessEntity> accesses;

	@OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
	private List<LinkEntity> links;

	private String fileName;
	private String fileKey;
	private String fileType;
	private Long fileSize;
	private String fileUrl;

}
