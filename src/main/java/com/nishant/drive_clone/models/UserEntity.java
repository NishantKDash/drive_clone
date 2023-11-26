package com.nishant.drive_clone.models;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity extends BaseModel {

	private String email;
	private String name;
	private String password;

	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
	private List<FileEntity> files;

	@OneToMany(mappedBy = "consumer" , cascade=CascadeType.ALL)
	private List<AccessEntity> accesses;
}
