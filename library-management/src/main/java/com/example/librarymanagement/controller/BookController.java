package com.example.librarymanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.librarymanagement.helper.Helper;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.repository.BookRepo;
import com.example.librarymanagement.service.ReportsService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.List;

@RestController
public class BookController {
	
	@Autowired
	BookRepo repo;
    @Autowired
    private JdbcTemplate jdbc;

    @RequestMapping(value = "/insertPage", method = RequestMethod.GET)
    public void showInsertPage(HttpServletResponse response) throws IOException {
        Connection con = null;
        Statement stmt = null;
        PrintWriter out = response.getWriter();
        try {
            con = jdbc.getDataSource().getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM book");

            out.println("<html><body>");
            out.println("<h3>Insert New Book</h3>");
            out.println("<form action=/insert method=POST>");
            out.println("<label for=\"author\">Author : </label>");
            out.println("<input type=\"text\" id=\"author\" name=\"author\"><br>");
            out.println("<label for=\"title\">Book Title:</label>");
            out.println("<input type=\"text\" id=\"title\" name=\"title\"><br>");
            out.println("<label for=\"copies\">Copies Count:</label>");
            out.println("<input type=\"text\" id=\"copies\" name=\"copies\"><br>");
            out.println("<button type=\"submit\">Insert Book</button>");
            out.println("</form>");

            out.println("<h3>Current Book Table</h3>");
            out.println("<table border=\"1\">");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>AUTHOR</th>");
            out.println("<th>TITLE</th>");
            out.println("<th>AVAILABILITY STATUS</th>");
            out.println("<th>COPIES</th>");
            out.println("</tr>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String author1 = rs.getString("author");
                String title1 = rs.getString("title");
                boolean available = rs.getBoolean("availability_status");
                String availability = available ? "Available" : "Not Available"; //short hand if else
                int copies1 = rs.getInt("copies");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + author1 + "</td>");
                out.println("<td>" + title1 + "</td>");
                out.println("<td>" + availability + "</td>");
                out.println("<td>" + copies1 + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");
        } catch (Exception e) {
            System.out.println("Error while fetching the data: " + e.getMessage());
            e.printStackTrace(response.getWriter());
        } 
    }
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public void insert(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Connection con = null;
        Statement stmt = null;
        PrintWriter out = response.getWriter();
        try {
            con = jdbc.getDataSource().getConnection();
            stmt = con.createStatement();

            String title = request.getParameter("title");
            String author = request.getParameter("author");
            int copies = Integer.parseInt(request.getParameter("copies"));
            boolean availabilityStatus = copies > 0;

            String sql = "INSERT INTO BOOK (title, author, availability_status, copies) VALUES ('" + title + "', '" + author + "', " + availabilityStatus + ", " + copies + ")";
            stmt.execute(sql);

            response.sendRedirect("/insertPage");
        } catch (Exception e) {
            System.out.println("Error while inserting the data: " + e.getMessage());
            e.printStackTrace(response.getWriter());
        }
    }

    @Autowired
    private ReportsService reportService;

    @RequestMapping(value="/excel", method = {RequestMethod.GET, RequestMethod.POST})
    public void generateExcelReport(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.ms-excel");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=books.xls";
        response.setHeader(headerKey, headerValue);
        
        reportService.generateExcel(response);
    }
    
    @PostMapping("/excelUpload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (Helper.checkExcelFormat(file)) {
            this.reportService.save(file);
            return ResponseEntity.ok("File uploaded successfully");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please insert a valid Excel file");
    }

    
    @RequestMapping(value="/show", method = RequestMethod.GET)
    public void show(HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Connection con = null;
        Statement stmt = null;
        try {
            con = jdbc.getDataSource().getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM book");
            List<Book> books=repo.findAll();
            out.println("<html><body>");
            out.println("<form action=\"/search_by_author\" method=\"POST\">");
            out.println("<input type=\"submit\" value=\"Search by author\"/>");
            out.println("<input type=\"text\" name=\"author_to_search\" placeholder=\"Enter author name\"/>");
            out.println("</form>");
            
            out.println("<table border=\"1\">");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>AUTHOR</th>");
            out.println("<th>TITLE</th>");
            out.println("<th>AVAILABILITY STATUS</th>");
            out.println("<th>COPIES</th>");
            out.println("</tr>");
            for(Book book: books) {
            	boolean avail=book. isAvailability_Status() ;
            	String Availability =avail ? "Available" :"Not Available";
            	 out.println("<tr>");
                 out.println("<td>" + book.getId() + "</td>");
                 out.println("<td>" + book.getAuthor() + "</td>");
                 out.println("<td>" + book.getTitle() + "</td>");
                 out.println("<td>" + Availability + "</td>");
                 out.println("<td>" + book.getCopies() + "</td>");
                 out.println("</tr>");
             }
  
             out.println("</table>");           
             out.println("<h3><a href=\"/excel\">Download books As excel File</a></h3>");
             out.println("</body></html>");

        } catch (Exception e) {
            System.out.println("Error while fetching the data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public void edit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String title_from = request.getParameter("title");
        String copies_new_str = request.getParameter("copies");

        if (title_from == null || title_from.isEmpty() || copies_new_str == null || copies_new_str.isEmpty()) {
            out.println("Error: Missing required parameters 'title' or 'copies'.");
            return;
        }

        int copies_new;
        try {
            copies_new = Integer.parseInt(copies_new_str);
        } catch (NumberFormatException e) {
            out.println("Error: 'copies' parameter is not a valid integer.");
            return;
        }

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            con = jdbc.getDataSource().getConnection();
            stmt = con.createStatement();
            int rowsAffected = stmt.executeUpdate("UPDATE book SET copies = " + copies_new + " WHERE title = '" + title_from + "'");
            
            if (rowsAffected == 0) {
                out.println("No record found with the specified title.");
                return;
            }

            rs = stmt.executeQuery("SELECT * FROM book WHERE title = '" + title_from + "'");

            out.println("<html><body>");
            out.println("<table border=\"1\">");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>AUTHOR</th>");
            out.println("<th>TITLE</th>");
            out.println("<th>AVAILABILITY STATUS</th>");
            out.println("<th>COPIES</th>");
            out.println("</tr>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String author = rs.getString("author");
                String title = rs.getString("title");
                boolean available = rs.getBoolean("Availability_Status");
                String availability = available ? "Available" : "Not Available";
                int copies = rs.getInt("copies");

                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + author + "</td>");
                out.println("<td>" + title + "</td>");
                out.println("<td>" + availability + "</td>");
                out.println("<td>" + copies + "</td>");
                out.println("</tr>");
            }
 
            out.println("</table>");
            out.println("</body></html>");

        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().println("Error retrieving data: " + e.getMessage());
        }
    }
    @RequestMapping(value="/delete")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Connection con = null;
        Statement stmt = null;
        try {
            con = jdbc.getDataSource().getConnection();
            stmt = con.createStatement();
            int id1=Integer.parseInt(request.getParameter("id"));
            String ti=request.getParameter("tittle");
            int rowsAffected = stmt.executeUpdate("DELETE FROM book WHERE id=" + id1);
             rowsAffected = stmt.executeUpdate("DELETE FROM book WHERE title='" + ti + "'");            
            if (rowsAffected > 0) {
                out.println("Data deleted successfully");
            } else {
                out.println("No record found with the specified id");
            } 
//            repo.deleteById(id1);
            ResultSet rs = stmt.executeQuery("SELECT * FROM book");

            out.println("<html><body>");
            out.println("<table border=\"1\">");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>AUTHOR</th>");
            out.println("<th>TITLE</th>");
            out.println("<th>AVAILABILITY STATUS</th>");
            out.println("<th>COPIES</th>");
            out.println("</tr>");

            while (rs.next()) {
                int id = rs.getInt("id");
                String author = rs.getString("author");
                String title = rs.getString("title");
                Boolean available=rs.getBoolean("Availability_Status");
                String Availabilty;
                if(available) {
                Availabilty="Available";}
                else {Availabilty="Not Available";}
                int copies=rs.getInt("Copies");
                out.println("<tr>");
                out.println("<td>" + id + "</td>");
                out.println("<td>" + author + "</td>");
                out.println("<td>" + title + "</td>");
                out.println("<td>" + Availabilty + "</td>");
                out.println("<td>" + copies + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body></html>");
            
            
        } catch (Exception e) {
            System.out.println("Error while deleting the data: " + e.getMessage());
            e.printStackTrace(out);
        } 
    }
    
    @RequestMapping(value = "/search_by_author", method = RequestMethod.POST)
    public void getBooksByAuthor(@RequestParam("author_to_search") String author, HttpServletResponse response) throws IOException {
        List<Book> books = repo.findByAuthor(author);
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h2>Books by " + author + "</h2>");
        if (books.isEmpty()) {
            out.println("<p>No books found for author: " + author + "</p>");
        } else {
            out.println("<table border=\"1\">");
            out.println("<tr>");
            out.println("<th>ID</th>");
            out.println("<th>AUTHOR</th>");
            out.println("<th>TITLE</th>");
            out.println("<th>AVAILABILITY STATUS</th>");
            out.println("<th>COPIES</th>");
            out.println("</tr>");

            for (Book book : books) {
                out.println("<tr>");
                out.println("<td>" + book.getId() + "</td>");
                out.println("<td>" + book.getAuthor() + "</td>");
                out.println("<td>" + book.getTitle() + "</td>");
                boolean available = book.isAvailability_Status();
                String availability = available ? "Available" : "Not Available";
                out.println("<td>" + availability + "</td>");
                out.println("<td>" + book.getCopies() + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
        }

        out.println("<a href=\"/show\">Back to all books</a>");
        out.println("</body></html>");
    }
    

    
}
