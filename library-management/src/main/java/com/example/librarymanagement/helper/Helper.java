package com.example.librarymanagement.helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.example.librarymanagement.model.Book;

public class Helper {

	
	public static boolean checkExcelFormat(MultipartFile file) {
		
		String contentType=file.getContentType();
		if(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return true; 
		}
		else {return false;}
		
	}
	public static List<Book> convertExcelToListOfBooks(InputStream is) {
	    List<Book> list = new ArrayList<>();
	    try (XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(is)) {
	        XSSFSheet sheet = workbook.getSheet("Sheet1");

	        if (sheet == null) {
	            throw new IllegalStateException("Sheet 'data' not found in the Excel file");
	        }

	        Iterator<Row> iterator = sheet.iterator();
	        int rowNumber = 0;

	        while (iterator.hasNext()) {
	            Row row = iterator.next();

	            if (rowNumber == 0 || isRowEmpty(row)) {
	                rowNumber++;
	                continue;
	            }

	            Book book = new Book();
	            Iterator<Cell> cells = row.cellIterator();
	            int cid = 0;

	            while (cells.hasNext()) {
	                Cell cell = cells.next();
	                switch (cid) {
	                    case 0 -> {
	                        if (cell.getCellType() == CellType.NUMERIC) {
	                            book.setId((long) cell.getNumericCellValue());
	                        }
	                    }
	                    case 1 -> {
	                        if (cell.getCellType() == CellType.STRING) {
	                            book.setAuthor(cell.getStringCellValue());
	                        }
	                    }
	                    case 2 -> {
	                        if (cell.getCellType() == CellType.STRING) {
	                            book.setTitle(cell.getStringCellValue());
	                        }
	                    }
	                    case 3 -> {
	                        if (cell.getCellType() == CellType.STRING) {
	                            String avail = cell.getStringCellValue();
	                            book.setAvailability_Status("AVAILABLE".equalsIgnoreCase(avail));
	                        }
	                    }
	                    case 4 -> {
	                        if (cell.getCellType() == CellType.NUMERIC) {
	                            book.setCopies((int) cell.getNumericCellValue());
	                        }
	                    }
	                    default -> {}
	                }
	                cid++;
	            }
	            list.add(book);
	            rowNumber++;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}

	private static boolean isRowEmpty(Row row) {
	    for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
	        Cell cell = row.getCell(c);
	        if (cell != null && cell.getCellType() != CellType.BLANK) {
	            return false;
	        }
	    }
	    return true;
	}


	
}
