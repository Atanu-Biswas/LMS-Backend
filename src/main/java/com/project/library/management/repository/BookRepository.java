package com.project.library.management.repository;

//import java.util.Optional;

import com.project.library.management.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Long>{
    Book  findBookByTitle(String name);
}
