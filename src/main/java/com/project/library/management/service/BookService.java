package com.project.library.management.service;

import com.project.library.management.controller.AdminController;
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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private static final Logger logger = Logger.getLogger(BookService.class);
  private final BookRepository bookRepository;
  private final PublisherService publisherService;

  public boolean addBook(BookDTO bookDTO) throws PublisherNotFoundException {
    Publisher publisher = publisherService.getPublisherById(bookDTO.getPublisherId());
    logger.debug("Query fired for getting Publisher by Id");
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
    logger.debug("Saved the Book details in the database");
    return true;
  }

   public BookDTO getBookByName  (String name) throws BookNotFoundException{
    Book book=bookRepository.findBookByTitle(name);
    logger.debug("Query fired for getting the Book by BookName");
    //Publisher publisher=publisherService.getPublisherById(book.getPublisher().getPublisherId());
    if (book == null) {
        logger.error("Book Not Found Exception generated !");
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
    logger.debug("Query fired for Finding the Book by Author Name");
   
     if (books.isEmpty()) {
         logger.error("Author Not Found Exception generated !");
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
    logger.debug("Query fired for finding the Book by Id");
        if (book == null) {
            logger.error("Book Id Not Found Exception generated !");
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
    logger.debug("Enabled/Disabled the book by Id");

    return !isEnable;
   }
}
