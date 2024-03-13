package com.project.library.management.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.library.management.entity.Publisher;


public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByPublisherId(Long publisherId);
}
