package com.project.library.management.service;


import org.springframework.stereotype.Service;


import com.project.library.management.entity.Publisher;
import com.project.library.management.exception.PublisherNotFoundException;
import com.project.library.management.repository.PublisherRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;
    
    public Publisher getPublisherById(Long Id) throws PublisherNotFoundException {
        return publisherRepository.findById(Id).orElseThrow(PublisherNotFoundException::new);
    }

}
