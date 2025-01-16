package com.example.shopapp.controllers;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.shopapp.configurations.ExcelConfig;
import com.example.shopapp.models.Product;
import com.example.shopapp.responses.excel.ImportExcelResponse;
import com.example.shopapp.services.excel.ExcelService;
import com.example.shopapp.utils.ExcelExport;
import com.example.shopapp.utils.ExcelHelper;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/excel")
public class ExcelController {

  @Autowired
  ExcelService fileService;
  
  private ExcelConfig excelConfig;

  
//  private ProductService productService;

  @PostMapping("/import")
  public ResponseEntity<ImportExcelResponse> uploadFile(@RequestParam("file") MultipartFile file) {
    String message = "";

    if (ExcelHelper.hasExcelFormat(file)) {
      try {
        fileService.save(file);

        message = "Import the file successfully: " + file.getOriginalFilename();
        return ResponseEntity.status(HttpStatus.OK).body(new ImportExcelResponse(message));
      } catch (Exception e) {
        message = "Could not import the file: " + file.getOriginalFilename() + "!";
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ImportExcelResponse(message));
      }
    }

    message = "Please import an excel file!";
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ImportExcelResponse(message));
  }

  @GetMapping("/products-import")
  public ResponseEntity<List<Product>> getAllProducts() {
    try {
      List<Product> products = fileService.getAllProducts();

      if (products.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(products, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  @GetMapping("/export-to-excel")
  public void exportIntoExcelFile(HttpServletResponse response) throws IOException {
      response.setContentType("application/octet-stream");
      DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
      String currentDateTime = dateFormatter.format(new Date());

      String headerKey = "Content-Disposition";
      String headerValue = "attachment; filename=student" + currentDateTime + ".xlsx";
      response.setHeader(headerKey, headerValue);

      List <Product> listOfProducts = fileService.getAllProducts();
      
      ExcelExport generator = new ExcelExport(listOfProducts);
      
//      generator.generateExcelFile(response);
//      String sheetName = "Product List";
//      String titleName = "List of Products";
//      String[] headers = {"ID", "Name", "Price", "Description", "Category"};
      
      generator.generateExcelFile(response, 
              excelConfig.getSheetName(), 
              excelConfig.getTitleName(), 
              excelConfig.getHeaders());
      
  }
}
