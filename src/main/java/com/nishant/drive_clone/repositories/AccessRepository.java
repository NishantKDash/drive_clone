package com.nishant.drive_clone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nishant.drive_clone.models.AccessEntity;
import com.nishant.drive_clone.models.FileEntity;
import com.nishant.drive_clone.models.UserEntity;

public interface AccessRepository extends JpaRepository<AccessEntity, Long> {

	@Query("SELECT a FROM AccessEntity a WHERE a.owner = :owner AND a.consumer = :consumer AND a.file = :file")
	public AccessEntity accessByOwnerConsumerFile(@Param("owner") UserEntity owner, @Param("consumer") UserEntity consumer,
			@Param("file") FileEntity file);
}
