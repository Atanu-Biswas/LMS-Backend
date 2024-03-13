package com.project.library.management.repository;

import com.project.library.management.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByPublisherId(Long publisherId);

//    void deleteByPublisherId(Long publisherId);
}
