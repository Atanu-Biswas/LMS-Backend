package com.project.library.management.repository;

//import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

//import com.project.library.management.dto.BookDTO;
//import com.project.library.management.dto.BookDTO;
import com.project.library.management.entity.Book;

public interface BookRepository extends JpaRepository<Book,Long>{
    public  Book findBookBytitle(String name);
}
