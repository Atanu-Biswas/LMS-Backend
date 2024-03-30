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
import java.util.Date;
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

    public ByteArrayOutputStream generateReport(JRBeanCollectionDataSource dataSource,String jrxmlFileName,String username,Date startDate, Date endDate) throws FileNotFoundException, JRException, ReportGenerationException{
        //Load jrxml file and compile it.
        File file=ResourceUtils.getFile("classpath:templates\\"+jrxmlFileName);
        JasperReport jasperReport=JasperCompileManager.compileReport(file.getAbsolutePath());
        
        Map<String,Object> parameters=new HashMap<>();
        parameters.put("generatedBy", username);
        parameters.put("startDate", startDate);
        parameters.put("endDate", endDate);
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

    public ByteArrayOutputStream getAdminReport(String username,String reportName,Date startDate, Date endDate) throws FileNotFoundException, ReportGenerationException, JRException{

        switch(reportName){
            case "BookCollectionReport":
                return generateBooksCollectionReport(startDate,endDate,username);

            case "BookIssuanceSummary":
                return generateBookIssuanceSummaryReport(startDate,endDate,username);

            case "BookReturnSummary":
                return generateBookReturnSummaryReport(startDate,endDate,username);

            case "AllUserDataReport":
                return generateAllUserDataReport(username);

            case "BookRequestReport":
                return generateBookRequestReport(startDate,endDate,username);

            case "AllPublisherDataReport":
                return generateAllPublisherDataReport(username);

            default:
                throw new ReportGenerationException("Failed to generate report. Invalid reportName: "+reportName);
        }
    }

    public ByteArrayOutputStream getUserReport(String username,String reportName,Date startDate, Date endDate) throws FileNotFoundException, ReportGenerationException, JRException{

        switch(reportName){
            case "BookCollectionReport":
                return generateBooksCollectionReport(startDate,endDate,username);
            
            case "UserBookIssuanceSummary":
                return generateUserBookIssuanceSummaryReport(startDate,endDate,username);

            case "UserRequestSummary":
                return generateUserRequestSummaryReport(startDate,endDate,username);

            default:
                throw new ReportGenerationException("Failed to generate report. Invalid reportName: "+reportName);
        }
    }
    
    public ByteArrayOutputStream generateBooksCollectionReport(Date startDate,Date endDate,String username) throws JRException, FileNotFoundException, ReportGenerationException{
        List<Book> books = bookRepository.findByDateAddedBetween(startDate, endDate);

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

        return generateReport(dataSource,jrxmlFileName,username,startDate,endDate);
    }

    public ByteArrayOutputStream generateBookIssuanceSummaryReport(Date startDate, Date endDate,String username) throws FileNotFoundException, ReportGenerationException, JRException{
        List<BookIssue> bookIssues=bookIssueRepository.findByIssueDateBetween(startDate,endDate);

        List<BookIssueReportDTO> bookIssueReportDTOs=bookIssues.stream().map(bookIssue->
            BookIssueReportDTO.builder()
                            .issueId(bookIssue.getIssueId())
                            .bookTitle(bookIssue.getBook().getTitle())
                            .username(bookIssue.getUser().getUsername())
                            .issueDate(bookIssue.getIssueDate())
                            .build())
                            .toList();

        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(bookIssueReportDTOs);
        String jrxmlFileName="BookIssuanceSummary.jrxml";

        return generateReport(dataSource,jrxmlFileName,username,startDate,endDate);
    }

    public ByteArrayOutputStream generateBookReturnSummaryReport(Date startDate, Date endDate,String username) throws FileNotFoundException, ReportGenerationException, JRException{
        List<BookIssue> bookIssues=bookIssueRepository.findByReturnDateBetween(startDate,endDate);

        List<BookIssueReportDTO> bookIssueReportDTOs=bookIssues.stream().map(bookIssue->
            BookIssueReportDTO.builder()
                            .issueId(bookIssue.getIssueId())
                            .bookTitle(bookIssue.getBook().getTitle())
                            .username(bookIssue.getUser().getUsername())
                            .returnDate(bookIssue.getReturnDate())
                            .build())
                            .toList();

        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(bookIssueReportDTOs);
        String jrxmlFileName="BookReturnSummary.jrxml";

        return generateReport(dataSource,jrxmlFileName,username,startDate,endDate);
    }

    public ByteArrayOutputStream generateUserBookIssuanceSummaryReport(Date startDate, Date endDate,String username) throws FileNotFoundException, ReportGenerationException, JRException{//modify this function and report name
        List<BookIssue> bookIssues=bookIssueRepository.findByUserUsernameAndIssueDateBetweenOrUserUsernameAndReturnDateBetween(username,startDate,endDate,username,startDate,endDate);

        List<BookIssueReportDTO> bookIssueReportDTOs=bookIssues.stream().map(bookIssue->
            BookIssueReportDTO.builder()
                            .issueId(bookIssue.getIssueId())
                            .bookTitle(bookIssue.getBook().getTitle())
                            .issueDate(bookIssue.getIssueDate())
                            .returnDate(bookIssue.getReturnDate())
                            .build())
                            .toList();

        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(bookIssueReportDTOs);
        String jrxmlFileName="UserBookIssuanceSummary.jrxml";

        return generateReport(dataSource,jrxmlFileName,username,startDate,endDate);
    }

    public ByteArrayOutputStream generateUserRequestSummaryReport(Date startDate, Date endDate,String username) throws FileNotFoundException, ReportGenerationException, JRException{
        List<BookRequest> bookRequests=bookRequestRepository.findByUserUsernameAndRequestDateBetween(username,startDate,endDate);

        List<BookRequestReportDTO> bookRequestReportDTOs=bookRequests.stream().map(bookRequest->
                BookRequestReportDTO.builder()
                                    .requestId(bookRequest.getRequestId())
                                    .bookTitle(bookRequest.getBook().getTitle())
                                    .requestDate(bookRequest.getRequestDate())
                                    .weeksRequested(bookRequest.getWeeksRequested())
                                    .status(bookRequest.getStatus())
                                    .build())
                                    .toList();
        
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(bookRequestReportDTOs);
        String jrxmlFileName="UserRequestSummary.jrxml";

        return generateReport(dataSource,jrxmlFileName,username,startDate,endDate);
    }

    public ByteArrayOutputStream generateAllUserDataReport(String username) throws FileNotFoundException, JRException, ReportGenerationException{
        List<User> users=userRepository.findAll();

        List<UserReportDTO> userReportDTOs=users.stream().map(user->
            UserReportDTO.builder()
                        .username(user.getUsername())
                        .name(user.getName())
                        .mobileNo(user.getMobileNo())
                        .address(user.getAddress())
                        .role(user.getRole().toString())
                        .build())
                        .toList();
        
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(userReportDTOs);
        String jrxmlFileName="UserDataReport.jrxml";

        return generateReport(dataSource,jrxmlFileName,username,null,null);
    }

    public ByteArrayOutputStream generateBookRequestReport(Date startDate, Date endDate,String username) throws FileNotFoundException, JRException, ReportGenerationException{
        List<BookRequest> bookRequests=bookRequestRepository.findByRequestDateBetween(startDate, endDate);

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

        return generateReport(dataSource,jrxmlFileName,username,startDate,endDate);
    }

    public ByteArrayOutputStream generateAllPublisherDataReport(String username) throws FileNotFoundException, JRException, ReportGenerationException{
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
        String jrxmlFileName="PublisherDataReport.jrxml";

        return generateReport(dataSource,jrxmlFileName,username,null,null);
    }
}
