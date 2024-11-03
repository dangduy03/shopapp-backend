package com.example.shopapp.services.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import com.example.shopapp.dtos.ProductDTO;
import com.example.shopapp.dtos.ProductImageDTO;
import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.responses.product.ProductResponse;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    
    Product getProductById(long id) throws Exception;
    
    Page<ProductResponse> getAllProducts(String keyword,
                                         Long categoryId, 
                                         PageRequest pageRequest);
    
    Product updateProduct(long id, ProductDTO productDTO) throws Exception;
    
    void deleteProduct(long id);
    
    boolean existsByName(String name);
    
    ProductImage createProductImage(Long productId,
            						ProductImageDTO productImageDTO) throws Exception;

    List<Product> findProductsByIds(List<Long> productIds);
    //String storeFile(MultipartFile file) throws IOException; //chuyển sang FileUtils
    //void deleteFile(String filename) throws IOException;

    Product likeProduct(Long userId, Long productId) throws Exception;
    
    Product unlikeProduct(Long userId, Long productId) throws Exception;
    
    List<ProductResponse> findFavoriteProductsByUserId(Long userId) throws Exception;
    
    void generateFakeLikes() throws Exception;
}
