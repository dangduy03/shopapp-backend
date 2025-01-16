package com.example.shopapp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.example.shopapp.models.Category;
import com.example.shopapp.models.Product;

public class ExcelHelper {
	  public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	  static String[] HEADERs = { "Id", "Name", "Price","Description", "CategoryID" };
//	  static String SHEET = "Products";
//	  String sheetName = "RandomSheet";

	  public static boolean hasExcelFormat(MultipartFile file) {

	    if (!TYPE.equals(file.getContentType())) {
	      return false;
	    }
	    return true;
	  }

	  public static List<Product> excelToProducts(InputStream is,  Function<Long, Category> findCategoryById) {
		    try {
		        Workbook workbook = new XSSFWorkbook(is);
		        
		        Sheet sheet = workbook.getSheetAt(0);
//		        Sheet sheet = workbook.getSheet(SHEET);
		        
		        Iterator<Row> rows = sheet.iterator();
		        
		        List<Product> products = new ArrayList<>();

		        Map<String, Integer> headerMap = new HashMap<>();
		        if (rows.hasNext()) {
		            Row headerRow = rows.next();
		            for (Cell cell : headerRow) {
		                headerMap.put(cell.getStringCellValue(), cell.getColumnIndex());
		            }
		        }

		        if (!headerMap.containsKey("Id") || 
		        	!headerMap.containsKey("Name") ||
		            !headerMap.containsKey("Price") || 
		            !headerMap.containsKey("Description") ||
		            !headerMap.containsKey("CategoryID")) {
		            throw new RuntimeException("Thiếu cột cần thiết trong file Excel");
		        }

		        while (rows.hasNext()) {
		            Row currentRow = rows.next(); 
		            Product product = new Product();

		            product.setId((long) currentRow.getCell(headerMap.get("Id")).getNumericCellValue());
		            product.setName(currentRow.getCell(headerMap.get("Name")).getStringCellValue());
		            product.setPrice((float) currentRow.getCell(headerMap.get("Price")).getNumericCellValue());
		            product.setDescription(currentRow.getCell(headerMap.get("Description")).getStringCellValue());
//		            product.setCategory((Category)currentRow.getCell(headerMap.get("CategoryID")).getNumericCellValue());
		            
		            long categoryId = (long) currentRow.getCell(headerMap.get("CategoryID")).getNumericCellValue();
		            Category category = findCategoryById(categoryId, null);
		            product.setCategory(category);
		            products.add(product);
		        }

		        workbook.close();
		        return products;
		    } catch (IOException e) {
		        throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		    }
	  }
		    
	  private static Category findCategoryById(long categoryId , Map<Long, Category> categoryMap) {
		return categoryMap.getOrDefault(categoryId, null);
	  }

	  private static Map<String, Integer> extractHeaderMap(Iterator<Row> rows) {
		 return null;
	  }
}
	  
//	  public static List<Product> excelToProducts(InputStream is) {
//	    try {
//	      Workbook workbook = new XSSFWorkbook(is);
//
//	      Sheet sheet = workbook.getSheet(SHEET);
//	      Iterator<Row> rows = sheet.iterator();
//
//	      List<Product> products = new ArrayList<Product>();
//
//	      int rowNumber = 0;
//	      while (rows.hasNext()) {
//	        Row currentRow = rows.next();
//
//	        // skip header
//	        if (rowNumber == 0) {
//	          rowNumber++;
//	          continue;
//	        }
//
//	        Iterator<Cell> cellsInRow = currentRow.iterator();
//
//	        Product product = new Product();
//
//	        int cellIdx = 0;
//	        while (cellsInRow.hasNext()) {
//	          Cell currentCell = cellsInRow.next();
//
//	          switch (cellIdx) {
//	          case 0:
//	        	  product.setId((long) currentCell.getNumericCellValue());
//	            break;
//
//	          case 1:
//	        	  product.setName(currentCell.getStringCellValue());
//	            break;
//	            
//	          case 2:
//	        	  product.setPrice((float)currentCell.getNumericCellValue());
//	            break;
//
//	          case 3:
//	        	  product.setDescription(currentCell.getStringCellValue());
//	            break;
//
//	          case 4:
//	              long categoryId = (long) currentCell.getNumericCellValue();
//	              Category category = findCategoryById(categoryId);  // Thay thế bằng phương thức thực tế để tìm Category
//	              product.setCategory(category);
//	              break;
//
//	          default:
//	            break;
//	          }
//
//	          cellIdx++;
//	        }
//
//	        products.add(product);
//	      }
//
//	      workbook.close();
//
//	      return products;
//	    } catch (IOException e) {
//	      throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
//	    }
//	  }
//
//	private static Category findCategoryById(long categoryId) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//}
