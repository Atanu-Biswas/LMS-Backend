package com.project.library.management.service;


import com.project.library.management.dto.PublisherDTO;
import com.project.library.management.entity.Publisher;
import com.project.library.management.exception.PublisherNotFoundException;
import com.project.library.management.repository.PublisherRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PublisherService {


    private final PublisherRepository publisherRepository;

    public Publisher addPublisher(PublisherDTO publisherDTO){
        Publisher publisher = new Publisher();
        publisher.setName(publisherDTO.getName());
        publisher.setEmail(publisherDTO.getEmail());
        publisher.setPhoneNo(publisherDTO.getPhoneNo());

        Publisher savedPublisher = publisherRepository.save(publisher);
        return savedPublisher;
    }

    public PublisherDTO getPublisher(Long publisherId) throws PublisherNotFoundException{
        Publisher publisher= publisherRepository.findByPublisherId(publisherId).orElseThrow(()->new PublisherNotFoundException());
        PublisherDTO publisherDTO = new PublisherDTO();
        publisherDTO.setName(publisher.getName());
        publisherDTO.setPhoneNo(publisher.getPhoneNo());
        publisherDTO.setEmail(publisher.getEmail());

        return publisherDTO;
    }

    public void updatePublisher(PublisherDTO publisherDTO, Long id) {
        Publisher publisher = publisherRepository.findByPublisherId(id).orElseThrow(PublisherNotFoundException::new);
        publisher.setName(publisherDTO.getName());
        publisher.setEmail(publisherDTO.getEmail());
        publisher.setPhoneNo(publisherDTO.getPhoneNo());

        publisherRepository.save(publisher);
    }

    @Transactional
    public void deletePublisher(Long publisherId){
        publisherRepository.deleteById(publisherId);
    }
}
