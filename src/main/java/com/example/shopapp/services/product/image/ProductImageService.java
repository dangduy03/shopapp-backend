package com.example.shopapp.services.product.image;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.repositorys.ProductImageRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService{
    private final ProductImageRepository productImageRepository;
    
    @Override
    @Transactional
    public ProductImage deleteProductImage(Long id) throws Exception {
        Optional<ProductImage> productImage = productImageRepository.findById(id);
        if(productImage.isEmpty()) {
            throw new DataNotFoundException(
                    String.format("Cannot find product image with id: %ld", id)
            );
        }
        
        productImageRepository.deleteById(id);
        return productImage.get();
    }
}
