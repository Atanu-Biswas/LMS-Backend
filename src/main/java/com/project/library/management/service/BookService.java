package com.project.library.management.service;

import com.project.library.management.dto.BookDTO;
import com.project.library.management.dto.BookPaginationDTO;
import com.project.library.management.entity.Book;
import com.project.library.management.entity.Genre;
import com.project.library.management.entity.Publisher;
import com.project.library.management.exception.*;
import com.project.library.management.repository.BookIssueRepository;
import com.project.library.management.repository.BookRepository;
import com.project.library.management.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
  private static final Logger logger = Logger.getLogger(BookService.class);
  private final BookRepository bookRepository;
  private final PublisherRepository publisherRepository;
  private final PublisherService publisherService;
  private final BookIssueRepository bookIssueRepository;

  public boolean addBook(BookDTO bookDTO) throws PublisherNotFoundException, IsbnLengthException, IllegalQuantityException, IllegalPrintYear{

    if(bookDTO.getQuantityAvailable() <= 0){
      logger.error("Quantity should be greater than 0");
      throw new IllegalQuantityException("Quantity should be greater than 0");
    }

    if(bookDTO.getPrintYear() > new Date().getYear() + 1900 || bookDTO.getPrintYear() < 0){
      logger.error("Print year should be less than or equal to current year");
      throw new IllegalPrintYear("Print year should be less than or equal to current year");
    }

    if(bookDTO.getIsbn().length()!=13){
      logger.error("ISBN length should be 13 characters.");
      throw new IsbnLengthException("ISBN length should be 13 characters.");
    }

    Publisher publisher = publisherRepository.findById(bookDTO.getPublisherId()).orElseThrow(()->new PublisherNotFoundException("Publisher corresponding to given book not found!!"));
    logger.debug("Query fired for getting Publisher by Id");
    Book book = new Book();
    book.setAuthor(bookDTO.getAuthor());
    book.setTitle(bookDTO.getTitle());
    Genre genre = Genre.valueOf(bookDTO.getGenre().toUpperCase());
    book.setGenre(genre);
    book.setIsbn(bookDTO.getIsbn());
    book.setPrintYear(bookDTO.getPrintYear());
    book.setQuantityAvailable(bookDTO.getQuantityAvailable());
    book.setActive(bookDTO.isActive());
    book.setPublisher(publisher);
    book.setDateAdded(new Date());
    bookRepository.save(book);
    logger.debug("Saved the Book details in the database");
    return true;
  }

  public BookPaginationDTO getBookListByTitle(String title, int pageNumber, int pageSize, String sortBy, String sortDir)
      throws BookNotFoundException {
    Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

    Pageable p = PageRequest.of(pageNumber, pageSize, sort);
    Page<Book> bookPage = bookRepository.findByTitleStartingWithIgnoreCase(title, p);
    logger.debug("Query fired for getting the Book by title");
    if (bookPage.isEmpty()) {
      logger.error("Book Not Found Exception generated !");
      throw new BookNotFoundException("Book with title '" + title + "' not found");
    }
    List<Book> books = bookPage.getContent();

    List<BookDTO> bookDTOs = books.stream().map(book -> convertToDTO(book)).toList();

    return BookPaginationDTO.builder()
        .content(bookDTOs)
        .pageNumber(pageNumber)
        .pageSize(pageSize)
        .totalElements(bookPage.getTotalElements())
        .totalPages(bookPage.getTotalPages())
        .isLastPage(bookPage.isLast())
        .build();
  }

  public BookDTO getBookById(Long id) throws BookNotFoundException {
    Book book = bookRepository.findByBookId(id);
    if (book == null) {
      throw new BookNotFoundException("Book with title '" + id + "' not found");
    }
    BookDTO bookDTO = new BookDTO();
    bookDTO.setBookId(book.getBookId());
    bookDTO.setAuthor(book.getAuthor());
    bookDTO.setTitle(book.getTitle());
    bookDTO.setGenre(String.valueOf(book.getGenre()));
    bookDTO.setIsbn(book.getIsbn());
    bookDTO.setPrintYear(book.getPrintYear());
    bookDTO.setQuantityAvailable(book.getQuantityAvailable());
    bookDTO.setActive(bookDTO.isActive());
    bookDTO.setPublisherId(book.getPublisher().getPublisherId());
    return bookDTO;
  }

  public BookPaginationDTO getBooksByAuthor(String author, int pageNumber, int pageSize, String sortBy, String sortDir) throws BookNotFoundException{

    Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

    Pageable p = PageRequest.of(pageNumber, pageSize, sort);
    Page<Book> bookPage = bookRepository.findByAuthorStartingWithIgnoreCase(author, p);
    if (bookPage.isEmpty()) {
      logger.error("Author Not Found Exception generated !");
      throw new BookNotFoundException("No books found by author '" + author + "'");
    }

    List<Book> books = bookPage.getContent();
    logger.debug("Query fired for Finding the Book by Author Name");

    List<BookDTO> bookDTOs = books.stream().map(book -> convertToDTO(book)).toList();

    return BookPaginationDTO.builder()
        .content(bookDTOs)
        .pageNumber(pageNumber)
        .pageSize(pageSize)
        .totalElements(bookPage.getTotalElements())
        .totalPages(bookPage.getTotalPages())
        .isLastPage(bookPage.isLast())
        .build();
  }

//  public List<BookDTO> getBooksByPublisher(String publisherName) throws BookNotFoundException {
//
//    List<Book> books = bookRepository.findByPublisher(publisherName);
//
//    if (books.isEmpty()) {
//      throw new BookNotFoundException("No books found for publisher '" + publisherName + "'");
//    }
//
//    List<BookDTO> bookDTOs = new ArrayList<>();
//    for (Book book : books) {
//      BookDTO bookDTO = new BookDTO();
//      bookDTO.setAuthor(book.getAuthor());
//      bookDTO.setTitle(book.getTitle());
//      bookDTO.setGenre(book.getGenre());
//      bookDTO.setIsbn(book.getIsbn());
//      bookDTO.setPrintYear(book.getPrintYear());
//      bookDTO.setQuantityAvailable(book.getQuantityAvailable());
//      bookDTO.setActive(book.isActive());
//      bookDTO.setPublisherId(book.getPublisher().getPublisherId());
//      bookDTOs.add(bookDTO);
//    }
//
//    return bookDTOs;
//  }

  public boolean enableDisableBook(Long id) throws BookNotFoundException {
    Book book = bookRepository.findById(id).orElseThrow(()->new BookNotFoundException("Book with Id " + id + " not found"));
    logger.debug("Query fired for finding the Book by Id");

    boolean isEnable = true;

    if (isEnable == book.isActive()) {
      book.setActive(false);
      System.out.println("here");
      isEnable = false;
    } else {
      book.setActive(true);
    }
    bookRepository.save(book);
    logger.debug("Enabled/Disabled the book by Id");

    return !isEnable;
  }

  public void updateBook(Long bookId, BookDTO updateBookRequest) throws BookNotFoundException {
    Book book = bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException());

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
    bookDTO.setGenre(String.valueOf(book.getGenre()));
    bookDTO.setIsbn(book.getIsbn());
    bookDTO.setPrintYear(book.getPrintYear());
    bookDTO.setQuantityAvailable(book.getQuantityAvailable());
    bookDTO.setActive(book.isActive());
    bookDTO.setPublisherId(book.getPublisher().getPublisherId());
    return bookDTO;
  }

  private BookDTO convertToDTO(Book book) {

    return BookDTO.builder()
        .bookId(book.getBookId())
        .title(book.getTitle())
        .author(book.getAuthor())
        .isbn(book.getIsbn())
        .printYear(book.getPrintYear())
        .genre(String.valueOf(book.getGenre()))
        .quantityAvailable(book.getQuantityAvailable())
        .isActive(book.isActive())
        .publisherId(book.getPublisher().getPublisherId())
        .build();
  }

  public BookPaginationDTO getAllBooks(int pageNumber, int pageSize, String sortBy, String sortDir)
      throws BookNotFoundException {

    Sort sort = (sortDir.equalsIgnoreCase("asc")) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

    Pageable p = PageRequest.of(pageNumber, pageSize, sort);
    Page<Book> bookPage = bookRepository.findAll(p);
    logger.debug("Query fired for finding all Books");
    if (bookPage.isEmpty()) {
      logger.error("Book Not Found Exception generated!!");
      throw new BookNotFoundException("Books not found!!");
    }

    List<Book> books = bookPage.getContent();

    List<BookDTO> bookDTOs = books.stream().map(book -> {
      BookDTO bookDTO = convertToDTO(book);
      return bookDTO;
    }).collect(Collectors.toList());

    return BookPaginationDTO.builder()
        .content(bookDTOs)
        .pageNumber(pageNumber)
        .pageSize(pageSize)
        .totalElements(bookPage.getTotalElements())
        .totalPages(bookPage.getTotalPages())
        .isLastPage(bookPage.isLast())
        .build();
  }

  // keep this service
  @Transactional
  public boolean deleteBook(Long bookId) throws BookNotDeletedException {
    boolean isBookIssued=bookIssueRepository.existsByBookId(bookId);

    if(isBookIssued==true){
      throw new BookNotDeletedException("Book with ID " + bookId + " can not be deleted");
    }

      bookRepository.deleteByBookId(bookId);
      return true;

  }
}
