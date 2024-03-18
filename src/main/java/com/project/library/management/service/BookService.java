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
import com.project.library.management.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private static final Logger logger = Logger.getLogger(BookService.class);
  private final BookRepository bookRepository;
  private final PublisherRepository publisherRepository;
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

    public List<BookDTO> getBookListByTitle (String name) throws BookNotFoundException{
        List<Book> books=bookRepository.findBookListByTitle(name);
        logger.debug("Query fired for getting the Book by BookName");
        //Publisher publisher=publisherService.getPublisherById(book.getPublisher().getPublisherId());
        if (books == null) {
            logger.error("Book Not Found Exception generated !");
            throw new BookNotFoundException("Book with title '" + name + "' not found");
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
            bookDTO.setActive(bookDTO.isActive());
            bookDTO.setPublisherId(book.getPublisher().getPublisherId());
            bookDTOs.add(bookDTO);
        }

        return bookDTOs;
    }

    public BookDTO getBookById  (Long id) throws BookNotFoundException{
        Book book=bookRepository.findByBookId(id);
        //Publisher publisher=publisherService.getPublisherById(book.getPublisher().getPublisherId());
        if (book == null) {
            throw new BookNotFoundException("Book with title '" + id + "' not found");
        }
        BookDTO bookDTO=new BookDTO();
        bookDTO.setBookId(book.getBookId());
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

  public List<BookDTO> getBooksByPublisher(String publisherName) throws PublisherNotFoundException {

    List<Book> books = bookRepository.findByPublisher(publisherName);

    if (books.isEmpty()) {
      throw new PublisherNotFoundException("No books found for publisher '" + publisherName + "'");
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

    public void updateBook(Long bookId, BookDTO updateBookRequest) {
        Book book = bookRepository.findByBookId(bookId);

        // Update book properties based on DTO values
        book.setTitle(updateBookRequest.getTitle());
        book.setAuthor(updateBookRequest.getAuthor());

        bookRepository.save(book);
    }



    public BookDTO getBookByName(String name) {
        Book book = bookRepository.findBookByTitle(name);
        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setImageUrl(book.getImageUrl());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setPrintYear(book.getPrintYear());
        bookDTO.setQuantityAvailable(book.getQuantityAvailable());
        bookDTO.setActive(book.isActive());
        bookDTO.setPublisherId(book.getPublisher().getPublisherId());
        return bookDTO;
    }


  public BookPaginationDTO getAllBooks(int pageNumber,int pageSize,String sortBy, String sortDir){

    Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

    Pageable p=PageRequest.of(pageNumber, pageSize, sort);
    Page<Book> bookPage = bookRepository.findAll(p);

    List<Book> books = bookPage.getContent();

    List<BookDTO> bookDTOs=books.stream().map(book->{
      BookDTO bookDTO=new BookDTO();
      bookDTO.setBookId(book.getBookId());
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
