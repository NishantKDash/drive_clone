package com.nishant.drive_clone.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nishant.drive_clone.models.LinkEntity;

public interface LinkRepository extends JpaRepository<LinkEntity, Long> {
   public LinkEntity getLinkFromlinkCode(String linkCode);
}
