package com.project.library.management.service;

import com.project.library.management.dto.BookDTO;
import com.project.library.management.entity.Book;
import com.project.library.management.entity.Publisher;
import com.project.library.management.exception.AuthorNotFoundException;
import com.project.library.management.exception.BookIdNotFoundException;
import com.project.library.management.exception.BookNotFoundException;
import com.project.library.management.exception.PublisherNotFoundException;
import com.project.library.management.repository.BookRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;
  private final PublisherService publisherService;

  public boolean addBook(BookDTO bookDTO) throws PublisherNotFoundException {
    Publisher publisher = publisherService.getPublisherById(bookDTO.getPublisherId());
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

   public BookDTO getBookByName  (String name) throws BookNotFoundException{
    Book book=bookRepository.findBookByTitle(name);
    //Publisher publisher=publisherService.getPublisherById(book.getPublisher().getPublisherId());
    if (book == null) {
      throw new BookNotFoundException("Book with title '" + name + "' not found");
  }
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

   public List<BookDTO> getBooksByAuthor  (String author) throws AuthorNotFoundException{
   
    List<Book> books =  bookRepository.findByAuthorIgnoreCase(author);

   
     if (books.isEmpty()) {
        throw new AuthorNotFoundException("No books found for author '" + author + "'");
    }

    List<BookDTO> bookDTOs = new ArrayList<>();
    for (Book book : books) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setImageUrl(book.getImageUrl());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setPrintYear(book.getPrintYear());
        bookDTO.setQuantityAvailable(book.getQuantityAvailable());
        bookDTO.setActive(book.isActive());
        bookDTO.setPublisherId(book.getPublisher().getPublisherId());
        bookDTOs.add(bookDTO);
    }
    
    return bookDTOs;
   }

   public boolean enableDisableBook(Long id) throws BookIdNotFoundException{
    Book book=bookRepository.findByBookId(id);
        if (book == null) {
            throw new BookIdNotFoundException("Book with ID " + id + " not found");
        }
    boolean isEnable=true;

    if(isEnable==book.isActive()){
      book.setActive(false);
      System.out.println("here");
      isEnable=false;
    }
    else{
      book.setActive(true);
    }
    bookRepository.save(book);

    return !isEnable;
   }
}
