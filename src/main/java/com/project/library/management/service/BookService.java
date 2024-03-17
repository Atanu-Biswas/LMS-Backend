package com.project.library.management.service;

import com.project.library.management.dto.BookDTO;
import com.project.library.management.dto.BookPaginationDTO;
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
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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


  public BookPaginationDTO getAllBooks(int pageNumber,int pageSize,String sortBy, String sortDir){

    Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

    Pageable p=PageRequest.of(pageNumber, pageSize, sort);
    Page<Book> bookPage = bookRepository.findAll(p);

    List<Book> books = bookPage.getContent();

    List<BookDTO> bookDTOs=books.stream().map(book->{
      BookDTO bookDTO=new BookDTO();
      bookDTO.setTitle(book.getTitle());
      bookDTO.setAuthor(book.getAuthor());
      bookDTO.setIsbn(book.getIsbn());
      bookDTO.setPrintYear(book.getPrintYear());
      bookDTO.setImageUrl(book.getImageUrl());
      bookDTO.setQuantityAvailable(book.getQuantityAvailable());
      bookDTO.setActive(book.isActive());
      bookDTO.setPublisherId(book.getPublisher().getPublisherId());
      return bookDTO;
    }).collect(Collectors.toList());

    BookPaginationDTO bookPaginationDTO= new BookPaginationDTO();
    bookPaginationDTO.setContent(bookDTOs);
    bookPaginationDTO.setPageNumber(pageNumber);
    bookPaginationDTO.setPageSize(pageSize);
    bookPaginationDTO.setTotalElements(bookPage.getTotalElements());
    bookPaginationDTO.setTotalPages(bookPage.getTotalPages());
    bookPaginationDTO.setLastPage(bookPage.isLast());

    return bookPaginationDTO;
  }
}
