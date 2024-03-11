package com.project.library.management.service;



import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.project.library.management.dto.BookDTO;
import com.project.library.management.entity.Book;
import com.project.library.management.entity.Publisher;
import com.project.library.management.exception.PublisherNotFoundException;
import com.project.library.management.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;
  private final PublisherService publisherService;
  
  public boolean addBook(BookDTO bookDTO) throws PublisherNotFoundException {
   Publisher publisher=publisherService.getPublisherById(bookDTO.getPublisherId());
   Book book = new Book();
   book.setAuthor(bookDTO.getAuthor());
   book.setTitle(bookDTO.getTitle());
   book.setImageUrl(bookDTO.getImageUrl());
   book.setIsbn(bookDTO.getIsbn());
   book.setPrintYear(bookDTO.getPrintYear());
   book.setQuantityAvailable(bookDTO.getQuantityAvailable());
   book.setActive(bookDTO.isActive());
   book.setPublisher(publisher);
   bookRepository.save(book);
   return true;
 }
   public BookDTO getBookByName(String name){
    Book book=bookRepository.findBookBytitle(name);
    //Publisher publisher=publisherService.getPublisherById(book.getPublisher().getPublisherId());
    BookDTO bookDTO=new BookDTO();
    bookDTO.setAuthor(book.getAuthor());
    bookDTO.setTitle(book.getTitle());
    bookDTO.setImageUrl(book.getImageUrl());
    bookDTO.setIsbn(book.getIsbn());
    bookDTO.setPrintYear(book.getPrintYear());
    bookDTO.setQuantityAvailable(book.getQuantityAvailable());
    bookDTO.setActive(bookDTO.isActive());
    bookDTO.setPublisherId(book.getPublisher().getPublisherId());
    return bookDTO;
   }
}
