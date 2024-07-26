package com.example.librarymanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.librarymanagement.model.Book;



public interface BookRepo extends JpaRepository<Book,Long> {

	List<Book> findByAuthor(String author);
	
	List<Book> findByTitle(String title);
	
	List<Book> findAll();
	
//	List<Book> deleteById(Long Id);
	
}
