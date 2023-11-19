package com.nishant.drive_clone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nishant.drive_clone.models.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity,Long>{

}
