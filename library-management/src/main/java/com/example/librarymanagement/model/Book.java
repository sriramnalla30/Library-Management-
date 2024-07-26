package com.example.librarymanagement.model;

import jakarta.persistence.*;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private boolean Availability_Status;
    private int Copies;

    
    
	public Book() {
	}
	public Book(Long id, String title, String author, boolean availability_Status, int copies) {
		super();
		this.id = id;
		this.title = title;
		this.author = author;
		Availability_Status = availability_Status;
		Copies = copies;
	}
	public boolean isAvailability_Status() {
		return Availability_Status;
	}
	public void setAvailability_Status(boolean availability_Status) {
		Availability_Status = availability_Status;
	}
	public int getCopies() {
		return Copies;
	}
	public void setCopies(int copies) {
		Copies = copies;
	}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
}
