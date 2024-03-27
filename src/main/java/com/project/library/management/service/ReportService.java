package com.project.library.management.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.project.library.management.dto.reportDTO.BookIssueReportDTO;
import com.project.library.management.dto.reportDTO.BookReportDTO;
import com.project.library.management.dto.reportDTO.BookRequestReportDTO;
import com.project.library.management.dto.reportDTO.PublisherReportDTO;
import com.project.library.management.dto.reportDTO.UserReportDTO;
import com.project.library.management.entity.Book;
import com.project.library.management.entity.BookIssue;
import com.project.library.management.entity.BookRequest;
import com.project.library.management.entity.Publisher;
import com.project.library.management.entity.User;
import com.project.library.management.repository.BookIssueRepository;
import com.project.library.management.repository.BookRepository;
import com.project.library.management.repository.BookRequestRepository;
import com.project.library.management.repository.PublisherRepository;
import com.project.library.management.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookIssueRepository bookIssueRepository;
    private final BookRequestRepository bookRequestRepository;
    private final PublisherRepository publisherRepository;

    public String generateReport(String reportFormat,JRBeanCollectionDataSource dataSource,String jrxmlFileName,String reportFileName) throws FileNotFoundException, JRException{
        //Load jrxml file and compile it.
        File file=ResourceUtils.getFile("classpath:templates\\"+jrxmlFileName);
        JasperReport jasperReport=JasperCompileManager.compileReport(file.getAbsolutePath());
        
        Map<String,Object> parameters=new HashMap<>();
        parameters.put("generatedBy", "Team LMS");
        JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport,parameters,dataSource);

        String destinationPath="src\\main\\resources\\static";
        if(reportFormat.equalsIgnoreCase("html")){
            JasperExportManager.exportReportToHtmlFile(jasperPrint,destinationPath+"\\"+reportFileName+".html");
            
        }
        else if(reportFormat.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile(jasperPrint,destinationPath+"\\"+reportFileName+".pdf");
        }

        return "Report generated successfully in path: "+destinationPath;
    }
    
    public String generateBooksCollectionReport(String reportFormat) throws FileNotFoundException, JRException{
        List<Book> books = bookRepository.findAll();

        List<BookReportDTO> bookReportDTOs=books.stream().map(book->
            BookReportDTO.builder()
                        .bookId(book.getBookId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .genre(book.getGenre())
                        .isbn(book.getIsbn())
                        .printYear(book.getPrintYear())
                        .quantityAvailable(book.getQuantityAvailable())
                        .activeStatus((book.isActive()) ? "ACTIVE":"INACTIVE")
                        .publisherName(book.getPublisher().getName())
                        .build())
                        .toList();
        
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(bookReportDTOs);
        String jrxmlFileName="BookCollection.jrxml";
        String reportFileName="booksCollectionReport";

        return generateReport(reportFormat,dataSource,jrxmlFileName,reportFileName);
    }

    public String generateUserDataReport(String reportFormat) throws FileNotFoundException, JRException{
        List<User> users=userRepository.findAll();

        List<UserReportDTO> userReportDTOs=users.stream().map(user->
            UserReportDTO.builder()
                        .username(user.getUsername())
                        .name(user.getName())
                        .mobileNo(user.getMobileNo())
                        .address(user.getAddress())
                        .role(user.getRole())
                        .build())
                        .toList();
        
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(userReportDTOs);
        String jrxmlFileName="UserData.jrxml";
        String reportFileName="UserDataReport";

        return generateReport(reportFormat,dataSource,jrxmlFileName,reportFileName);
    }

    public String generateBookIssueReport(String reportFormat) throws FileNotFoundException, JRException{
        List<BookIssue> bookIssues=bookIssueRepository.findAll();

        List<BookIssueReportDTO> bookIssueReportDTOs=bookIssues.stream().map(bookIssue->
            BookIssueReportDTO.builder()
                            .issueId(bookIssue.getIssueId())
                            .bookTitle(bookIssue.getBook().getTitle())
                            .username(bookIssue.getUser().getUsername())
                            .issueDate(bookIssue.getIssueDate())
                            .dueDate(bookIssue.getDueDate())
                            .returnDate(bookIssue.getReturnDate())
                            .fine(bookIssue.getFine())
                            .fineStatus(bookIssue.getFineStatus())
                            .build())
                            .toList();
        
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(bookIssueReportDTOs);
        String jrxmlFileName="BookIssueReport.jrxml";
        String reportFileName="BookIssueReport";

        return generateReport(reportFormat,dataSource,jrxmlFileName,reportFileName);
    }

    public String generateBookRequestReport(String reportFormat) throws FileNotFoundException, JRException{
        List<BookRequest> bookRequests=bookRequestRepository.findAll();

        List<BookRequestReportDTO> bookRequestReportDTOs=bookRequests.stream().map(bookRequest->
            BookRequestReportDTO.builder()
                                .requestId(bookRequest.getRequestId())
                                .bookTitle(bookRequest.getBook().getTitle())
                                .username(bookRequest.getUser().getUsername())
                                .requestDate(bookRequest.getRequestDate())
                                .weeksRequested(bookRequest.getWeeksRequested())
                                .status(bookRequest.getStatus())
                                .build())
                                .toList();
        
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(bookRequestReportDTOs);
        String jrxmlFileName="BookRequestReport.jrxml";
        String reportFileName="BookRequestReport";

        return generateReport(reportFormat,dataSource,jrxmlFileName,reportFileName);
    }

    public String generatePublisherReport(String reportFormat) throws FileNotFoundException, JRException{
        List<Publisher> publishers=publisherRepository.findAll();

        List<PublisherReportDTO> publisherReportDTOs=publishers.stream().map(publisher->
            PublisherReportDTO.builder()
                            .publisherId(publisher.getPublisherId())
                            .name(publisher.getName())
                            .email(publisher.getEmail())
                            .phoneNo(publisher.getPhoneNo())
                            .build())
                            .toList();
        
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(publisherReportDTOs);
        String jrxmlFileName="PublisherReport.jrxml";
        String reportFileName="PublisherReport";

        return generateReport(reportFormat,dataSource,jrxmlFileName,reportFileName);
    }
}
