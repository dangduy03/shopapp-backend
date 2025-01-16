package com.example.shopapp.utils;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.example.shopapp.models.Product;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

public class ExcelExport {
	
	private List < Product > productList;
	
    private XSSFWorkbook workbook;
    
    private XSSFSheet sheet;

    public ExcelExport(List < Product > productList) {
        this.productList = productList;
        workbook = new XSSFWorkbook();
    }
    
    private void writeHeader(String sheetName, String titleName, String[] headers) {
    	// sheet
        sheet = workbook.createSheet(sheetName);
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(20);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);

        createCell(row, 0, titleName, style);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headers.length - 1));
        font.setFontHeightInPoints((short) 10);
        
        createCell(row, 0, "ID", style);
        createCell(row, 1, "Name", style);
        createCell(row, 2, "Price", style);
        createCell(row, 3, "Description", style);
        createCell(row, 4, "CategoryID", style);

        row = sheet.createRow(1);
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        for (int i = 0; i < headers.length; i++) {
            createCell(row, i, headers[i], style);
        }
    }
    
    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
            
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
            
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
            
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        
        cell.setCellStyle(style);
    }
    
    private void write() {
        int rowCount = 1;
        
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        
        for (Product record: productList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            
            createCell(row, columnCount++, record.getId(), style);
            createCell(row, columnCount++, record.getName(), style);
            createCell(row, columnCount++, record.getPrice(), style);
            createCell(row, columnCount++, record.getDescription(), style);
            createCell(row, columnCount++, record.getCategory(), style);
        }
    }
    
    public void generateExcelFile(HttpServletResponse response, String sheetName, String titleName, String[] headers) throws IOException {
//      String sheetName = null;
//		String titleName = null;
//		String[] headers = null;
		writeHeader(sheetName, titleName, headers);
        write();
        
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        outputStream.close();
    }

}
