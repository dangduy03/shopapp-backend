package com.example.shopapp.services.excel;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.shopapp.models.Product;
import com.example.shopapp.repositorys.CategoryRepository;
import com.example.shopapp.repositorys.ProductRepository;
import com.example.shopapp.utils.ExcelHelper;

@Service
public class ExcelService {
  @Autowired
  ProductRepository productrepository;
  
  CategoryRepository categoryRepository;

  public void save(MultipartFile file) {
    try {
//      List<Product> products = ExcelHelper.excelToProducts(file.getInputStream());
    	// Truyền vào `Function<Long, Category>` dùng lambda
        List<Product> products = ExcelHelper.excelToProducts(file.getInputStream(), categoryId -> {
            return categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy Category với ID: " + categoryId));
        });
      productrepository.saveAll(products);
    } catch (IOException e) {
      throw new RuntimeException("fail to store excel data: " + e.getMessage());
    }
  }

  public List<Product> getAllProducts() {
    return productrepository.findAll();
  }
}
