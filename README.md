# Library-Management

Project Set-Up
1. Prerequisites
Before setting up the project, ensure you have the following installed on your system:
Java Development Kit (JDK) 17 or later.
Apache Maven (version 3.9.8 or later).
MySQL Database.
An IDE like IntelliJ IDEA, Eclipse, or VS Code.
Postman (for API testing).

a. Clone the Repository
If your project is hosted on GitHub or another version control system, clone the repository to your local machine:
      git clone https://github.com/sriramnalla30/library-management-system.git
      cd library-management-system
b. Import the Project
Open your IDE and import the project as a Maven project.
Maven should automatically resolve the dependencies specified in the pom.xml file.
c. Database Configuration
Create a database in MySQL for your project. For example, you can create a database named librarydb.
    CREATE DATABASE librarydb;
Configure the database settings in the application.properties or application.yml file located in src/main/resources. Here's an example of the configuration using application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/librarydb
spring.datasource.username=root
spring.datasource.password=ram123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
Adjust the username and password fields to match your MySQL credentials.

3. Running the Application
a. Using IDE
Locate the LibraryManagementApplication.java class in your IDE.
Right-click and run the application as a Java application.
b. Using Maven
Alternatively, you can use Maven to build and run the application from the command line:
{
mvn clean install
mvn spring-boot:run
}

4. Accessing the Application
Once the application is running, you can access it in a web browser at http://localhost:8080.

5. Using Postman for API Testing
You can use Postman to test the various endpoints:

Insert a new book: POST /insert
View all books: GET /show
Edit book copies: POST /edit
Delete a book: POST /delete
Generate Excel report: GET /generateExcelReport
Upload Excel file: POST /upload
6. Generating Excel Reports and Uploading Books
To generate an Excel report of all books, you can use the /generateExcelReport endpoint. To upload an Excel file with book data, use the /upload endpoint, ensuring the file format is correct.

7. Troubleshooting
Dependency Issues: Ensure all dependencies in the pom.xml file are correctly resolved.
Database Connection: Verify your MySQL credentials and database URL.
Port Conflicts: Ensure the default port 8080 is available or change it in the application.properties file using server.port.



1. BookController.java
Purpose: This is the main controller for handling HTTP requests related to books in the library management system. It manages CRUD operations and serves as the entry point for handling requests related to books.

Key Methods and Functionalities:

showInsertPage(HttpServletResponse response): Generates an HTML page for inserting new books and displaying the current books in the database.
insert(HttpServletRequest request, HttpServletResponse response): Handles the insertion of a new book into the database based on user input from the form.
generateExcelReport(HttpServletResponse response): Generates an Excel report of all books using the ReportsService.
upload(MultipartFile file): Handles the uploading of Excel files to insert new books, ensuring the file is in the correct format.
show(HttpServletResponse response): Displays all books in the database, providing a search form for filtering books by author.
edit(HttpServletRequest request, HttpServletResponse response): Allows editing the number of copies for a specific book identified by its title.
delete(HttpServletRequest request, HttpServletResponse response): Deletes a book record from the database by ID or title.
getBooksByAuthor(@RequestParam("author_to_search") String author, HttpServletResponse response): Searches and displays books by a specific author.

2. BookRepo.java
Purpose: This interface extends JpaRepository and provides methods for interacting with the database. It simplifies CRUD operations and custom queries.

Key Methods and Functionalities:

findByAuthor(String author): Finds a list of books by the specified author.
findByTitle(String title): Finds a list of books by the specified title.
findAll(): Retrieves all books in the database.

3. Book.java
Purpose: This class represents the Book entity in the application. It includes attributes that map to the columns in the database table and serves as a data model.

Key Fields:

id: The unique identifier for a book.
author: The author of the book.
title: The title of the book.
availabilityStatus: A boolean indicating whether the book is available.
copies: The number of copies available.
Annotations:

@Entity: Specifies that this class is an entity and will be mapped to a database table.
@Table(name = "book"): Specifies the table name in the database.
@Id, @GeneratedValue(strategy = GenerationType.IDENTITY): Specifies the primary key and its generation strategy.
@Column: Maps fields to database columns.
4. Helper.java
Purpose: This utility class provides helper methods, primarily for handling Excel file formats.

Key Methods and Functionalities:

checkExcelFormat(MultipartFile file): Checks if the uploaded file is in Excel format by verifying the file's content type and file extension.

5. ReportsService.java
Purpose: This service class handles business logic related to generating reports, such as exporting data to Excel files and saving data from Excel files to the database.

Key Methods and Functionalities:

generateExcel(HttpServletResponse response): Generates an Excel file containing all books from the database and writes it to the HTTP response.
save(MultipartFile file): Saves book data from an uploaded Excel file into the database.

6. pom.xml
Purpose: The Maven Project Object Model (POM) file manages the project's dependencies, build configuration, and project metadata.

Key Dependencies:

spring-boot-starter-thymeleaf: Thymeleaf templating engine for web applications.
poi-ooxml, poi, commons-collections4: Apache POI library for reading and writing Excel files.
spring-boot-starter-data-jdbc, spring-boot-starter-data-jpa, spring-boot-starter-jdbc: Spring Data and JDBC support for database interactions.
spring-boot-starter-web: Core web functionalities for building web applications.
spring-boot-devtools: Development tools for live reload and faster application restarts.
mysql-connector-j: MySQL database connector for JDBC.
