package com.project.library.management.service;

import com.project.library.management.dto.reportDTO.*;
import com.project.library.management.entity.*;
import com.project.library.management.exception.ReportGenerationException;
import com.project.library.management.repository.*;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookIssueRepository bookIssueRepository;
    private final BookRequestRepository bookRequestRepository;
    private final PublisherRepository publisherRepository;

    public ByteArrayOutputStream generateReport(JRBeanCollectionDataSource dataSource,String jrxmlFileName) throws FileNotFoundException, JRException, ReportGenerationException{
        //Load jrxml file and compile it.
        File file=ResourceUtils.getFile("classpath:templates\\"+jrxmlFileName);
        JasperReport jasperReport=JasperCompileManager.compileReport(file.getAbsolutePath());
        
        Map<String,Object> parameters=new HashMap<>();
        parameters.put("generatedBy", "Team LMS");
        JasperPrint jasperPrint=JasperFillManager.fillReport(jasperReport,parameters,dataSource);

        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        JRPdfExporter exporter=new JRPdfExporter();
        SimplePdfExporterConfiguration configuration=new SimplePdfExporterConfiguration();
        configuration.setCompressed(true);

        exporter.setConfiguration(configuration); 
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        exporter.exportReport();  
        
        if(byteArrayOutputStream == null || byteArrayOutputStream.size() == 0){
            throw new ReportGenerationException("Failed to generate report. Report data is empty or null.");
        }

        return byteArrayOutputStream;
    }

    public ByteArrayOutputStream getReport(String reportName) throws FileNotFoundException, ReportGenerationException, JRException{

        switch(reportName){
            case "BookCollectionReport":
                return generateBooksCollectionReport();

            default:
                throw new ReportGenerationException("Failed to generate report. Invalid reportName: "+reportName);
        }
    }
    
    public ByteArrayOutputStream generateBooksCollectionReport() throws JRException, FileNotFoundException, ReportGenerationException{
        List<Book> books = bookRepository.findAll();

        List<BookReportDTO> bookReportDTOs=books.stream().map(book->
            BookReportDTO.builder()
                        .bookId(book.getBookId())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .genre(String.valueOf(book.getGenre()))
                        .isbn(book.getIsbn())
                        .printYear(book.getPrintYear())
                        .quantityAvailable(book.getQuantityAvailable())
                        .activeStatus((book.isActive()) ? "ACTIVE":"INACTIVE")
                        .publisherName(book.getPublisher().getName())
                        .build())
                        .toList();
        
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(bookReportDTOs);
        String jrxmlFileName="BookCollection.jrxml";

        return generateReport(dataSource,jrxmlFileName);
    }

    public ByteArrayOutputStream generateUserDataReport(String reportFormat) throws FileNotFoundException, JRException, ReportGenerationException{
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

        return generateReport(dataSource,jrxmlFileName);
    }

    public ByteArrayOutputStream generateBookIssueReport(String reportFormat) throws FileNotFoundException, JRException, ReportGenerationException{
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

        return generateReport(dataSource,jrxmlFileName);
    }

    public ByteArrayOutputStream generateBookRequestReport(String reportFormat) throws FileNotFoundException, JRException, ReportGenerationException{
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

        return generateReport(dataSource,jrxmlFileName);
    }

    public ByteArrayOutputStream generatePublisherReport(String reportFormat) throws FileNotFoundException, JRException, ReportGenerationException{
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

        return generateReport(dataSource,jrxmlFileName);
    }
}
