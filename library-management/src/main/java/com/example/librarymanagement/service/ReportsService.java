package com.example.librarymanagement.service;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.librarymanagement.helper.Helper;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.repository.BookRepo;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
@Service
public class ReportsService {
    @Autowired
    private BookRepo bookrepo;

    public void generateExcel(HttpServletResponse response) throws IOException {
	        try (HSSFWorkbook workbook = new HSSFWorkbook(); 
	             ServletOutputStream ops = response.getOutputStream()) {

	            List<Book> books = bookrepo.findAll();
	            HSSFSheet sheet = workbook.createSheet("Books Info");
	            HSSFRow row = sheet.createRow(0);
	            
	            row.createCell(0).setCellValue("ID");
	            row.createCell(1).setCellValue("AUTHOR");
	            row.createCell(2).setCellValue("TITLE");
	            row.createCell(3).setCellValue("AVAILABILITY STATUS");
	            row.createCell(4).setCellValue("COPIES");

	            int dataRowIndex = 1;

	            for (Book book : books) {
	                HSSFRow dataRow = sheet.createRow(dataRowIndex++);
	                dataRow.createCell(0).setCellValue(book.getId());
	                dataRow.createCell(1).setCellValue(book.getAuthor());
	                dataRow.createCell(2).setCellValue(book.getTitle());
	                dataRow.createCell(3).setCellValue(book.isAvailability_Status() ? "Available" : "Not Available");
	                dataRow.createCell(4).setCellValue(book.getCopies());
	            }
	            workbook.write(ops);
	        } catch (IOException e) {
	            e.printStackTrace();
	            throw e;
	        }
	    }
    
    public void save(MultipartFile file) throws IOException {
    	List<Book> books=Helper.convertExcelToListOfBooks(file.getInputStream());
    	this.bookrepo.saveAll(books);
    	
    }
    
    public List<Book> getAllBooks(){
    	return this.bookrepo.findAll();
    	
    }
    
    }

