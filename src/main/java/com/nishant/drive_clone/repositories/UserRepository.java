package com.nishant.drive_clone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nishant.drive_clone.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	public UserEntity findByEmail(String email);
}
